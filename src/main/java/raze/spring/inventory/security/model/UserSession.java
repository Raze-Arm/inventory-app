package raze.spring.inventory.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class UserSession extends AppSession{
    @Id
    private String username;
    @Lob
    private  String token;

    @Builder
    public UserSession(String username, String token) {
        super(username, token);
        this.username = username;
        this.token = token;
    }


    public UserSession() {
        super();
    }
}
