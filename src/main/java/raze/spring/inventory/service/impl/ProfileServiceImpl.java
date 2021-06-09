package raze.spring.inventory.service.impl;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raze.spring.inventory.converter.UserProfileDtoToUserProfile;
import raze.spring.inventory.converter.UserProfileToUserProfileDto;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.repository.UserProfileRepository;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.service.ProfileService;
import raze.spring.inventory.utility.FileUploadUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    private final static String PHOTO_DIR = "files/images/user-photos/";
    private final UserProfileRepository userProfileRepository;
    private final UserProfileDtoToUserProfile profileDtoToProfile;
    private final UserProfileToUserProfileDto profileToProfileDto;
    private final PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileDtoToUserProfile profileDtoToProfile, UserProfileToUserProfileDto profileToProfileDto, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.profileDtoToProfile = profileDtoToProfile;
        this.profileToProfileDto = profileToProfileDto;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public Resource getUserPhoto(String username) throws MalformedURLException {
        final UserProfile profile = this.userProfileRepository.findByAccountUsername(username).orElse(null);
        String photoPath;
        if(profile == null || profile.getPhotoPath() == null) {
//            photoPath = PHOTO_DIR + "placeholder/profile-placeholder.jpg";
            photoPath = "profile-placeholder.jpg";
        } else {
            photoPath = profile.getPhotoPath();
        }
        log.debug("USER PHOTO PATH: {}",photoPath);
        Resource resource = new UrlResource(Paths.get(photoPath).toAbsolutePath().toUri());
        if(resource.exists()) return resource;
        else throw new MalformedURLException();
    }
    @Transactional
    @Override
    public ProfileDto getUserProfileByUsername(String username) {
        final UserProfile profile = this.userProfileRepository.findByAccountUsername(username).orElseThrow();
        return this.profileToProfileDto.convert(profile);
    }





    static void saveImageFile(ProfileDto profileDto, UserProfile profileToSave, String photoDir) throws IOException {
        MultipartFile file = profileDto.getPhoto();
        if(file != null){
            final String existingPhoto =  profileToSave.getPhotoPath();
            if(existingPhoto != null) java.nio.file.Files.deleteIfExists(Path.of(existingPhoto));
            String fileName = profileDto.getUsername()+ "." + Files.getFileExtension(file.getResource().getFilename());
//            String uploadDir = "files/images/user-photos/" ;
            FileUploadUtil.saveFile(photoDir, fileName, file);
            profileToSave.setPhotoPath(photoDir + fileName);
        }
    }

    @Transactional
    @Override
    public void updateUserProfile(ProfileDto profileDto) throws IOException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
        if (profileDto.getId() == null)
          throw new NoSuchElementException();

        final UserProfile profileToEdit = this.userProfileRepository.findById(profileDto.getId()).orElseThrow();
        profileToEdit.setFirstName(profileDto.getFirstName());
        profileToEdit.setLastName(profileDto.getLastName());

        final UserAccount account  = profileToEdit.getAccount();
        if(account != null) {
//            account.setUsername(userProfileDto.getUsername());
            final String password = profileDto.getPassword();
            if(password != null)account.setPassword(passwordEncoder.encode(password));
            profileToEdit.setAccount(account);
        }
        saveImageFile(profileDto, profileToEdit, PHOTO_DIR);
        this.userProfileRepository.save(profileToEdit);

    }
}
