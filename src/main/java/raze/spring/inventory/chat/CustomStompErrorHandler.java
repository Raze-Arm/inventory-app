package raze.spring.inventory.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.controller.handler.ErrorDetails;

@Slf4j
@Component
public class CustomStompErrorHandler extends StompSubProtocolErrorHandler {
    private final ObjectMapper objectMapper;

    public CustomStompErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage, Throwable ex)
    {
        Throwable exception = ex;
        log.debug("Stomp Error : {}", ex.getMessage());
        if (exception instanceof MessageDeliveryException)
        {
            exception = exception.getCause();
        }

        if (exception instanceof UnauthorizedException)
        {
            return handleUnauthorizedException(clientMessage, exception);
        }

        if (exception instanceof AccessDeniedException)
        {
            return handleAccessDeniedException(clientMessage, exception);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }



    private Message<byte[]> handleUnauthorizedException(Message<byte[]> clientMessage, Throwable ex) throws JsonProcessingException {
        ErrorDetails apiError = new ErrorDetails(HttpStatus.UNAUTHORIZED, ex.getMessage() , "Invalid Credential");

        return prepareErrorMessage(clientMessage, apiError, HttpStatus.UNAUTHORIZED.toString());

    }

    private Message<byte[]> handleAccessDeniedException(Message<byte[]> clientMessage, Throwable ex) throws JsonProcessingException {
        ErrorDetails apiError = new ErrorDetails(HttpStatus.FORBIDDEN, ex.getMessage() , "Access Denied");

        return prepareErrorMessage(clientMessage, apiError, HttpStatus.FORBIDDEN.toString());

    }

    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, ErrorDetails apiError, String errorCode) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(apiError);

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

//        setReceiptIdForClient(clientMessage, accessor);
        accessor.setMessage(errorCode);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message != null ? message.getBytes() : null, accessor.getMessageHeaders());
    }


}
