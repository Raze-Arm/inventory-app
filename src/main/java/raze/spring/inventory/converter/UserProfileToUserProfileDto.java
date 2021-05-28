package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.UserProfileDto;
import raze.spring.inventory.security.model.UserAccount;

@Component
public class UserProfileToUserProfileDto implements Converter<UserProfile, UserProfileDto> {
    @Synchronized
    @Override
    public UserProfileDto convert(UserProfile userProfile) {
        final UserAccount account = userProfile.getAccount();
        return UserProfileDto.builder()
                .id(userProfile.getId())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .username(account != null ? account.getUsername() : null)
                .build();
    }
}
