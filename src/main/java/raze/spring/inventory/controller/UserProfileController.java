package raze.spring.inventory.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.service.ProfileService;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
public class UserProfileController {
    private final ProfileService profileService;

    public UserProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }



    @GetMapping(path = {"/profile/{username}", "/profile/{username}/"})
    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable("username") String username) {
        return ResponseEntity.ok(this.profileService.getUserProfileByUsername(username));
    }

    @PutMapping(path = {"/profile", "/profile/"})
    @PreAuthorize("authentication.name == #profile.username or hasRole('ADMIN')")
    public ResponseEntity<Void> updateProfile(@Valid @ModelAttribute ProfileDto profile) throws IOException {
        this.profileService.updateUserProfile(profile);
        return ResponseEntity.ok().build();
    }
    @GetMapping(path = {"/download/profile/{username}"})
    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadProfilePhoto(@PathVariable("username") String username) throws MalformedURLException {
        return ResponseEntity.ok(this.profileService.getUserPhoto(username));
    }


}
