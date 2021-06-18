package raze.spring.inventory.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Slf4j
public class SocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public SocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @MessageMapping("/secured/user")
    public void send(@Valid  @Payload  AppSockMessage message, Principal user ) {
        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/queue/messages", message);
    }




}
