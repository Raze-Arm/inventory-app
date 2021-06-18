package raze.spring.inventory.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.Principal;

@Slf4j
@ControllerAdvice
public class MessageThreadControllerAdvice {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageThreadControllerAdvice(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /* MessageThreadController.createPost() validation exception are handled here. */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public void methodArgumentNotValidExceptionHandler(
            org.springframework.web.bind.MethodArgumentNotValidException e) {
        log.debug("methodArgumentNotValidExceptionHandler");
    }

    /* MessageThreadController.createMessage() validation exception are handled here. */
    @MessageExceptionHandler(org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException.class)
    public void methodArgumentNotValidWebSocketExceptionHandler(
            org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException e, Principal principal, AppSockMessage sockMessage) {
        log.debug("catching Message  MethodArgumentNotValidException ");
        sockMessage.setError(true);
        final String destination = "/queue/messages";
        this.simpMessagingTemplate.convertAndSendToUser(
            principal.getName(),
                destination,
                sockMessage
            );
        this.simpMessagingTemplate.convertAndSendToUser(
            sockMessage.getTo(),
            destination,
            AppSockMessage.builder()
                .from(sockMessage.getFrom())
                .to(sockMessage.getTo())
                .typing(false)
                .build());
    }


}