package raze.spring.inventory.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import raze.spring.inventory.converter.UserProfileDtoToUserProfile;
import raze.spring.inventory.converter.UserProfileToUserProfileDto;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.UserProfileDto;
import raze.spring.inventory.repository.UserProfileRepository;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.service.UserProfileService;
import raze.spring.inventory.utility.FileUploadUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileDtoToUserProfile profileDtoToProfile;
    private final UserProfileToUserProfileDto profileToProfileDto;
    private final PasswordEncoder passwordEncoder;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileDtoToUserProfile profileDtoToProfile, UserProfileToUserProfileDto profileToProfileDto, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.profileDtoToProfile = profileDtoToProfile;
        this.profileToProfileDto = profileToProfileDto;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UUID saveUserProfile(UserProfileDto userProfileDto) {
        final UserProfile userProfile = this.userProfileRepository.save(this.profileDtoToProfile.convert(userProfileDto));
        return userProfile.getId();
    }

    @Override
    public Resource getUserPhoto(String username) throws MalformedURLException {
        final Path path = Path.of("user-photos/");
        Path filePath = path.resolve(username).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if(resource.exists()) return resource;
        else throw new MalformedURLException();
    }

    @Transactional
    @Override
    public UserProfileDto getUserProfileByUsername(String username) {
        final UserProfile profile = this.userProfileRepository.findByAccountUsername(username).orElseThrow();
        return this.profileToProfileDto.convert(profile);
    }

    @Transactional
    @Override
    public void updateUserProfile(UserProfileDto userProfileDto) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!Objects.equals(username, userProfileDto.getUsername()) || userProfileDto.getId() == null)
          throw new NoSuchElementException();

        final UserProfile profileToEdit = this.userProfileRepository.findById(userProfileDto.getId()).orElseThrow();
        profileToEdit.setFirstName(userProfileDto.getFirstName());
        profileToEdit.setLastName(userProfileDto.getLastName());

        final UserAccount account  = profileToEdit.getAccount();
        if(account != null) {
//            account.setUsername(userProfileDto.getUsername());
            final String password = userProfileDto.getPassword();
            if(password != null)account.setPassword(passwordEncoder.encode(password));
            profileToEdit.setAccount(account);
        }
        MultipartFile file = userProfileDto.getFile();
        if(file != null){
//            String fileName = StringUtils.cleanPath(file.getResource());
            String fileName = userProfileDto.getUsername();
            String uploadDir = "user-photos/" ;
            FileUploadUtil.saveFile(uploadDir, fileName, file);
            profileToEdit.setPhotoPath(uploadDir + fileName);
        }
        this.userProfileRepository.save(profileToEdit);

    }
}
