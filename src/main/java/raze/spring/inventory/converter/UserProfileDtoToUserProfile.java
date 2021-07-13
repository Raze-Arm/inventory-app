package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.security.model.UserAccount;

@Component
public class UserProfileDtoToUserProfile implements Converter<ProfileDto, UserProfile> {
    private final PasswordEncoder passwordEncoder;

    public UserProfileDtoToUserProfile(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Synchronized
    @Override
    public UserProfile convert(ProfileDto profileDto) {
        final UserAccount userAccount =
            UserAccount.builder()
                .username(profileDto.getUsername())
                .password(passwordEncoder.encode(profileDto.getPassword()))
                    .userRoles(profileDto.getRole())
                .build();
        return UserProfile.builder()
                .firstName(profileDto.getFirstName())
                .lastName(profileDto.getLastName())
                .email(profileDto.getEmail())
                .imageAvailable(profileDto.hasImage())
                .account(userAccount)
                .build();
    }
}
