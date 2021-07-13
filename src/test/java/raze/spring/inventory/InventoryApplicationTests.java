package raze.spring.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@Profile("test")
@ActiveProfiles({"test", "dev", "mail"})
@SpringBootTest
class InventoryApplicationTests {

    @Test
    void contextLoads() {
    }

}
