package raze.spring.inventory.chat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;
import raze.spring.inventory.domain.dto.ProfileDto;
import raze.spring.inventory.security.service.UserSessionService;
import raze.spring.inventory.service.UserService;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final UserSessionService sessionService;
    private final UserService userService;
    private final CustomStompErrorHandler errorHandler;

    @Value("#{systemEnvironment['ALLOWED_ORIGINS'] ?: 'https://localhost:3000'}")
    private String originAddress;

    public WebSocketConfig(UserSessionService sessionService, UserService userService, CustomStompErrorHandler errorHandler) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.errorHandler = errorHandler;
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue" );
        registry.setUserDestinationPrefix("/secured/user");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(errorHandler).addEndpoint("/secured/chat").setAllowedOriginPatterns("originAddress").addInterceptors(httpSessionHandshakeInterceptor());
        registry.setErrorHandler(errorHandler).addEndpoint("/secured/chat")
                .setAllowedOrigins(originAddress)
                .withSockJS().setInterceptors(httpSessionHandshakeInterceptor());
    }
    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
                    HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
                    Cookie token = WebUtils.getCookie(servletRequest, "Authorization");
                    attributes.put("Authorization", token.getValue());
                }
                return true;
            }
            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            }
        };
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

    registration.interceptors(
        new ChannelInterceptor() {
          @SneakyThrows
          @Override
          public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
                final String token = sessionAttributes.get("Authorization").toString();
//              final String token =
//                  Optional.ofNullable(accessor.getFirstNativeHeader("Authorization")).orElse(null);
              final String username =
                  Optional.ofNullable(accessor.getFirstNativeHeader("login")).orElse(null);
              if (token != null && username != null) {
                final boolean isValid =
                    sessionService.isSessionValid(username, token.trim());
                if (isValid) {
                  final ProfileDto user = userService.getUserByUsername(username);
                  accessor.setUser(
                      new UsernamePasswordAuthenticationToken(
                          user.getUsername(), null, user.getRole().getGrantedAuthorities()));
                      OnlineUsers.addUser(
                          StompUser.builder()
                              .id(user.getId())
                              .username(user.getUsername())
                              .firstName(user.getFirstName())
                              .lastName(user.getLastName())
                              .build());
                }
              }
            }
              if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                  final Principal user = accessor.getUser();
                  if(user != null )
                  {
                      final String username = user.getName();
                      OnlineUsers.removeUserByUsername(username);
                  }
              }
              return message;
          }
        });
    }
}
