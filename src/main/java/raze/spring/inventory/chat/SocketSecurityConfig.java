package raze.spring.inventory.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
@Configuration
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {

        messages.simpTypeMatchers(SimpMessageType.CONNECT,
                SimpMessageType.DISCONNECT, SimpMessageType.OTHER).permitAll()
                .simpDestMatchers("/secured/chat/**").hasAuthority("BASIC")
                .anyMessage().hasAuthority("BASIC");
    }


    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }


}
