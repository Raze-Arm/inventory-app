package raze.spring.inventory.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raze.spring.inventory.Exception.ObjectNotFoundException;
import raze.spring.inventory.converter.UserProfileDtoToUserProfile;
import raze.spring.inventory.converter.UserProfileToUserProfileDto;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.repository.UserProfileRepository;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {
    private final static String PHOTO_DIR = "files/images/user-photos/";
    private final UserProfileRepository userProfileRepository;
    private final UserProfileDtoToUserProfile profileDtoToProfile;
    private final UserProfileToUserProfileDto profileToProfileDto;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserProfileRepository userProfileRepository, UserProfileDtoToUserProfile profileDtoToProfile, UserProfileToUserProfileDto profileToProfileDto, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.profileDtoToProfile = profileDtoToProfile;
        this.profileToProfileDto = profileToProfileDto;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Page<ProfileDto> getUsersPage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if(search == null || search.length() == 0) {
            return  this.userProfileRepository.findAll(pageable).map(this.profileToProfileDto::convert);
        } else {
            return  this.userProfileRepository.findAll(pageable, search).map(this.profileToProfileDto::convert);
        }
    }


    @Transactional
    @Override
    public Resource getUserPhoto(UUID id) throws MalformedURLException {
        final UserProfile profile = this.userProfileRepository.findById(id).orElse(null);
        return getPhotoResource(profile);
    }

    @Transactional
    @Override
    public Resource getUserPhotoByUsername(String username) throws MalformedURLException {
        final UserProfile profile = this.userProfileRepository.findByAccountUsername(username).orElse(null);
        return getPhotoResource(profile);
    }

    private Resource getPhotoResource(UserProfile profile) throws MalformedURLException {
        String photoPath;
        if(profile == null || profile.getPhotoPath() == null) {
//            photoPath = PHOTO_DIR + "placeholder/profile-placeholder.jpg";
            photoPath =  "profile-placeholder.jpg";
        } else {
            photoPath = profile.getPhotoPath();
        }
        Resource resource = new UrlResource(Paths.get(photoPath).toAbsolutePath().toUri());
        if(resource.exists()) return resource;
        else throw new MalformedURLException();
    }

    @Transactional
    @Override
    public ProfileDto getUser(UUID id) {
        final UserProfile profile = this.userProfileRepository.findById(id).orElseThrow();
        return this.profileToProfileDto.convert(profile);
    }

    @Transactional
    @Override
    public ProfileDto getUserByUsername(String username) throws UsernameNotFoundException {
        final UserProfile profile = this.userProfileRepository.findByAccountUsername(username).orElse(null);
        if(profile == null) throw new UsernameNotFoundException("Username not found");
        return  profile != null ? this.profileToProfileDto.convert(profile) : null;
    }

    private void saveImageFile(ProfileDto profileDto, UserProfile profileToSave) throws IOException {
        ProfileServiceImpl.saveImageFile(profileDto, profileToSave, PHOTO_DIR);
    }
    @Transactional
    @Override
    public UUID saveUser(ProfileDto profileDto) throws IOException {
        final UserProfile profileToSave = Objects.requireNonNull(this.profileDtoToProfile.convert(profileDto));
        saveImageFile(profileDto, profileToSave);
        final UserProfile userProfile = this.userProfileRepository.save(profileToSave);

        return userProfile.getId();
    }
    @Override
    public void updateUser(ProfileDto profileDto) throws IOException {
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
            account.setUserRoles(profileDto.getRole());
            profileToEdit.setAccount(account);
        }
        saveImageFile(profileDto, profileToEdit);
        this.userProfileRepository.save(profileToEdit);
    }

    @Override
    public void deleteUser(UUID id) {
        this.userProfileRepository.deleteById(id);
    }
}
