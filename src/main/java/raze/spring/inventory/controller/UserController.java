package raze.spring.inventory.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@RestController()
@RequestMapping("/v1")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping(path = {"/user", "/user/"})
    @PreAuthorize("hasRole('ADMIN') and  authentication.name.equals('admin')")
    public ResponseEntity<Page<ProfileDto>> getUsersPage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search ) {
        return ResponseEntity.ok(this.userService.getUsersPage(page,size,sort, search));
    }

    @PostMapping(path = {"/user", "/user/"})
    @PreAuthorize("hasRole('ADMIN') and  authentication.name.equals('admin')")
    public ResponseEntity<UUID> saveUserProfile(@Valid @ModelAttribute ProfileDto profile) throws IOException {
        return ResponseEntity.ok(this.userService.saveUser(profile));
    }

    @GetMapping(path = {"/user/{id}", "/user/{id}/"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.userService.getUser(id));
    }

    @GetMapping(path = {"/user"}, params = {"username"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileDto> getUserByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(this.userService.getUserByUsername(username));
    }


    @PutMapping(path = {"/user", "/user/"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserProfile(@Valid @ModelAttribute ProfileDto profile) throws IOException {
        this.userService.updateUser(profile);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = {"/download/user/{id}"})
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> downloadUserPhoto(@PathVariable UUID id) throws MalformedURLException {
        return ResponseEntity.ok(this.userService.getUserPhoto(id));
    }

    @GetMapping(path = {"/download/user"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadUserPhotoByUsername(@RequestParam("username")String username) throws MalformedURLException {
        return  ResponseEntity.ok(this.userService.getUserPhotoByUsername(username));
    }

    @DeleteMapping(path = {"/user/{id}", "/user/{id}/"})
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        this.userService.deleteUser(id);
        return  ResponseEntity.ok().build();
    }



}
