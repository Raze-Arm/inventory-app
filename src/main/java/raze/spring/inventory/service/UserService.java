package raze.spring.inventory.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import raze.spring.inventory.Exception.ObjectNotFoundException;
import raze.spring.inventory.domain.dto.ProfileDto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

public interface UserService {
    Page<ProfileDto> getUsersPage(int page, int size, String sort, String search);

    UUID saveUser(ProfileDto profileDto) throws IOException;

    Resource getUserPhoto(UUID id) throws MalformedURLException;
    Resource getUserSmallPhoto(UUID id) throws MalformedURLException;

    Resource getUserPhotoByUsername(String username) throws MalformedURLException;


    ProfileDto getUser(UUID id);

    ProfileDto getUserByUsername(String username) throws  UsernameNotFoundException;

    void updateUser(ProfileDto profileDto) throws IOException;

    void deleteUser(UUID id);

}
