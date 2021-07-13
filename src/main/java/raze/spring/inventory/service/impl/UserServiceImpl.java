package raze.spring.inventory.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raze.spring.inventory.Exception.EmailNotFoundException;
import raze.spring.inventory.converter.UserProfileDtoToUserProfile;
import raze.spring.inventory.converter.UserProfileToUserProfileDto;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.registration.Request;
import raze.spring.inventory.registration.token.ConfirmationToken;
import raze.spring.inventory.registration.token.ConfirmationTokenService;
import raze.spring.inventory.repository.UserProfileRepository;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.security.repository.UserAccountRepository;
import raze.spring.inventory.service.UserService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import static raze.spring.inventory.service.impl.ProductServiceImpl.findByFileName;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final static String PHOTO_DIR = "files/images/user-photos/";
    private final UserProfileRepository userProfileRepository;
    private final UserProfileDtoToUserProfile profileDtoToProfile;
    private final UserProfileToUserProfileDto profileToProfileDto;
    private final PasswordEncoder passwordEncoder;



    @Override
    public Page<ProfileDto> getUsersPage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        if(search == null || search.length() == 0) {
            return  this.userProfileRepository.findAll(pageable).map(this.profileToProfileDto::convert);
        } else {
            return  this.userProfileRepository.findAll(pageable, search).map(this.profileToProfileDto::convert);
        }
    }


    @Override
    public Resource getUserPhoto(String username) throws MalformedURLException {
        return getResource(username, ".original.");
    }

    @Override
    public Resource getUserSmallPhoto(String username) throws MalformedURLException {
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
    public ProfileDto getUser(UUID id) {
        final UserProfile profile = this.userProfileRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return this.profileToProfileDto.convert(profile);
    }

    @Override
    public ProfileDto getUserByUsername(String username) throws UsernameNotFoundException {
        final UserProfile profile = this.userProfileRepository.findByAccountUsername(username).orElse(null);
        return  profile != null ? this.profileToProfileDto.convert(profile) : null;
    }

    private void saveImageFile(ProfileDto profileDto) throws IOException {
        ProfileServiceImpl.saveImageFile(profileDto, PHOTO_DIR);
    }
    @Override
    public UUID saveUser(ProfileDto profileDto) throws IOException {
        final UserProfile profileToSave = Objects.requireNonNull(this.profileDtoToProfile.convert(profileDto));
        final UserProfile userProfile = this.userProfileRepository.save(profileToSave);
        profileDto.setId(userProfile.getId());
        if(profileDto.hasImage()) saveImageFile(profileDto);

        return userProfile.getId();
    }
    @Override
    public void updateUser(ProfileDto profileDto) throws IOException {
        updateUserProps(profileDto);
        if(profileDto.hasImage())saveImageFile(profileDto);
    }

    @NotNull
    @Transactional
    UserProfile updateUserProps(ProfileDto profileDto) {
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
            account.setUserRoles(profileDto.getRole());
            profileToEdit.setAccount(account);
        }
        this.userProfileRepository.save(profileToEdit);
        return profileToEdit;
    }

    @Override
    public void deleteUser(UUID id) {
        this.userProfileRepository.deleteById(id);
    }
}
