package raze.spring.inventory.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.dto.ProfileDto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

public interface UserService {
    Page<ProfileDto> getUsersPage(int page, int size, String sort, String search);

    UUID saveUser(ProfileDto profileDto) throws IOException;

    Resource getUserPhoto(UUID id) throws MalformedURLException;

    ProfileDto getUser(UUID id);

    void updateUser(ProfileDto profileDto) throws IOException;

    void deleteUser(UUID id);

}
