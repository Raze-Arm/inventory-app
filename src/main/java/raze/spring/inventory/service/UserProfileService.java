package raze.spring.inventory.service;

import org.springframework.core.io.Resource;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.domain.dto.UserProfileDto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

public interface UserProfileService {
     UUID saveUserProfile(UserProfileDto userProfileDto);

     Resource getUserPhoto(String username) throws MalformedURLException;

     UserProfileDto getUserProfileByUsername(String username);

     void updateUserProfile(UserProfileDto userProfileDto) throws IOException;
}
