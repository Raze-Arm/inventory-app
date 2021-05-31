package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.UserProfileDto;
import raze.spring.inventory.security.model.UserAccount;

@Component
public class UserProfileDtoToUserProfile implements Converter<UserProfileDto, UserProfile> {
    private final PasswordEncoder passwordEncoder;

    public UserProfileDtoToUserProfile(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Synchronized
    @Override
    public UserProfile convert(UserProfileDto userProfileDto) {
        final UserAccount userAccount =
            UserAccount.builder()
                .username(userProfileDto.getUsername())
                .password(passwordEncoder.encode(userProfileDto.getPassword()))
                .build();
        return UserProfile.builder()
                .firstName(userProfileDto.getFirstName())
                .lastName(userProfileDto.getLastName())
                .account(userAccount)
                .build();
    }
}
