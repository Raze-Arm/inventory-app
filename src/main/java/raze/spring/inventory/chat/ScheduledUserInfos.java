package raze.spring.inventory.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import raze.spring.inventory.chat.OnlineUsers;

@Service
@Slf4j
public class ScheduledUserInfos {
    private  final  SimpMessagingTemplate simpMessagingTemplate;

    public ScheduledUserInfos(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @Scheduled(fixedRate = 30000)
    public void sendMessage() {
        simpMessagingTemplate.convertAndSend("/queue/users", OnlineUsers.getUsers());

    }
}
