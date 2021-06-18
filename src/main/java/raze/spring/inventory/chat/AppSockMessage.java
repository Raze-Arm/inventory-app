package raze.spring.inventory.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppSockMessage {
    private String from;
    private String to;
    private String text;
    private boolean typing;
}
