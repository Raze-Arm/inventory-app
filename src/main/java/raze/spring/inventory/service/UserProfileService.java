package raze.spring.inventory.service;

import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.UserProfileDto;

import java.util.UUID;

public interface UserProfileService {
     UUID saveUserProfile(UserProfileDto userProfileDto);

     UserProfileDto getUserProfileByUsername(String username);

     void updateUserProfile(UserProfileDto userProfileDto);
}
