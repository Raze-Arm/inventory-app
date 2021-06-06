package raze.spring.inventory.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ProfileDto {
    private UUID id ;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UserRole role;
    private MultipartFile photo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;
}
