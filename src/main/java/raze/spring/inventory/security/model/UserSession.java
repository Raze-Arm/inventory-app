package raze.spring.inventory.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

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
