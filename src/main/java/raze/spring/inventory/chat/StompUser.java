package raze.spring.inventory.chat;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StompUser {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
}
