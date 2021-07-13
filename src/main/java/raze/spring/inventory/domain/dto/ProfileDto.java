package raze.spring.inventory.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import raze.spring.inventory.security.role.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private UUID id ;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UserRole role;
    private MultipartFile photo;
    private boolean imageAvailable;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;

    @JsonIgnore
    public boolean hasImage() {
        return this.photo != null;
    }

//    public String getUsername() {
//        return username;
//    }
}
