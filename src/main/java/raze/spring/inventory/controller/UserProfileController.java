package raze.spring.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.domain.dto.UserProfileDto;
import raze.spring.inventory.service.UserProfileService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
public class UserProfileController {
    private final UserProfileService profileService;

    public UserProfileController(UserProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(path = {"/user", "/user/"})
    public ResponseEntity<UUID> saveUserProfile(@Valid @RequestBody UserProfileDto profile) {
        return ResponseEntity.ok(this.profileService.saveUserProfile(profile));
    }

    @GetMapping(path = {"/user", "/user/"})
    public ResponseEntity<UserProfileDto> getUserProfile(@RequestParam("username") String username) {
        return ResponseEntity.ok(this.profileService.getUserProfileByUsername(username));
    }

    @PutMapping(path = {"/user", "/user/"})
    public ResponseEntity<Void> updateUserProfile(@Valid @ModelAttribute UserProfileDto profile) throws IOException {
        this.profileService.updateUserProfile(profile);
        return ResponseEntity.ok().build();
    }


}
