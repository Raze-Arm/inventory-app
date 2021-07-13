package raze.spring.inventory.registration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("email")
public class RegistrationProperties {
    private String activationLink;

}
