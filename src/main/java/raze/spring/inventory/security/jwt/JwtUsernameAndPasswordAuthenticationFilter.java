package raze.spring.inventory.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import raze.spring.inventory.security.AppUserDetail;
import raze.spring.inventory.security.model.UserSession;
import raze.spring.inventory.security.service.UserAccountService;
import raze.spring.inventory.security.service.UserSessionService;


import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserSessionService userSessionService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;


    public JwtUsernameAndPasswordAuthenticationFilter(UserSessionService userSessionService, AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey) {
        this.userSessionService = userSessionService;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
            );


            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        final String username = authResult.getName();
        final String token = Jwts.builder()
            .setSubject(username)
            .claim("authorities",authResult.getAuthorities() )
            .setIssuedAt(new Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays()).toString()))
            .signWith(secretKey)
            .compact();
        this.userSessionService.insertSession(new UserSession(username, token));
//        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix()+ " " + token);


        final Cookie cookie = new Cookie(jwtConfig.getAuthorizationHeader() ,  token );
        cookie.setHttpOnly(true);
        cookie.setMaxAge( 10 * 24 * 60 * 60);
        response.addCookie(cookie);
    }




}
