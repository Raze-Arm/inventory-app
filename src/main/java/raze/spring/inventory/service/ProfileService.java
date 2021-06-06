package raze.spring.inventory.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.ProfileDto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

public interface ProfileService {


     Resource getUserPhoto(String username) throws MalformedURLException;

     ProfileDto getUserProfileByUsername(String username);

     void updateUserProfile(ProfileDto profileDto) throws IOException;
}
