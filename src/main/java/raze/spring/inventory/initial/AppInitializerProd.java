package raze.spring.inventory.initial;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.security.role.UserRole;
import raze.spring.inventory.service.UserService;

import javax.transaction.Transactional;

@Profile("prod")
@Slf4j
@Component
public class AppInitializerProd implements CommandLineRunner {




    private final UserService userService;

    public AppInitializerProd(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.debug("Initializing  Data...");
        final ProfileDto profile = this.userService.getUserByUsername("admin");
        if(profile == null) {
            final ProfileDto profileDto =
                    ProfileDto.builder()
                            .firstName("raze")
                            .lastName("arm")
                            .username("admin")
                            .password("1234567890")
                            .role(UserRole.ADMIN)
                            .build();
            userService.saveUser(profileDto);
        }

    }
}
