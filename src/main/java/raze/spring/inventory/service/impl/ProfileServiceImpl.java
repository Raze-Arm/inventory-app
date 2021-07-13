package raze.spring.inventory.service.impl;

import com.google.common.io.Files;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import raze.spring.inventory.converter.UserProfileDtoToUserProfile;
import raze.spring.inventory.converter.UserProfileToUserProfileDto;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.repository.UserProfileRepository;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.service.ProfileService;
import raze.spring.inventory.utility.FileUploadUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static raze.spring.inventory.service.impl.ProductServiceImpl.findByFileName;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final static String PHOTO_DIR = "files/images/user-photos/";

    private final UserProfileRepository userProfileRepository;
    private final UserProfileToUserProfileDto profileToProfileDto;
    private final PasswordEncoder passwordEncoder;




    @Override
    public Resource getUserPhoto(String username) throws MalformedURLException {
        return getResource(username, ".original.");

    }

    @Override
    public Resource getUserSmallPhoto(String username) {
        return getResource(username, ".small.");
    }

    @Nullable
    private Resource getResource(String username, String s) {
        try {
            List<Path> result = findByFileName(Path.of(PHOTO_DIR + username), s);
            if (result.size() > 0) {
                Resource resource = new UrlResource(result.get(0).toAbsolutePath().toUri());
                if (resource.exists()) return resource;
                else throw new MalformedURLException();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }




    @Override
    public ProfileDto getUserProfileByUsername(String username) {
        final UserProfile profile = this.userProfileRepository.findByAccountUsername(username).orElseThrow();
        return this.profileToProfileDto.convert(profile);
    }





    static void saveImageFile(ProfileDto profileDto, String photoDir) throws IOException {
        MultipartFile file = profileDto.getPhoto();
            final String username = profileDto.getUsername();
            final boolean isDir = java.nio.file.Files.isDirectory(Path.of(photoDir + username));
            if(isDir) FileUtils.deleteDirectory(new File(photoDir + username));
            String fileName =  Date.from(Instant.now()).toString() + ".original." + Files.getFileExtension(file.getResource().getFilename());
            FileUploadUtil.saveFile(photoDir + username + "/" , fileName, file);
    }

    @Override
    public void updateUserProfile(ProfileDto profileDto) throws IOException {

        updateProfileProps(profileDto);
        if(profileDto.hasImage()) saveImageFile(profileDto, PHOTO_DIR);

    }
    @Transactional
    void updateProfileProps(ProfileDto profileDto) {
        if (profileDto.getId() == null)
          throw new NoSuchElementException();

        final UserProfile profileToEdit = this.userProfileRepository.findById(profileDto.getId()).orElseThrow();
        profileToEdit.setFirstName(profileDto.getFirstName());
        profileToEdit.setLastName(profileDto.getLastName());
        profileToEdit.setEmail(profileDto.getEmail());
        if(profileDto.hasImage()) profileToEdit.setImageAvailable(true);
        final UserAccount account  = profileToEdit.getAccount();
        if(account != null) {
//            account.setUsername(userProfileDto.getUsername());
            final String password = profileDto.getPassword();
            if(password != null)account.setPassword(passwordEncoder.encode(password));
            profileToEdit.setAccount(account);
        }
        this.userProfileRepository.save(profileToEdit);
    }
}
