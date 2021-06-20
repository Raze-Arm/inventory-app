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

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Entity
public class UserSession {
    @Id
    private String username;
    @Lob
    private  String token;
}
