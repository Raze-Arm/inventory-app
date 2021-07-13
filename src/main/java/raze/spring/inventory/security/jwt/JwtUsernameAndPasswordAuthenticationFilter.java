package raze.spring.inventory.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import raze.spring.inventory.security.model.AppSession;
import raze.spring.inventory.security.model.UserSession;
import raze.spring.inventory.security.service.UserSessionService;


import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserSessionService userSessionService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;



    public JwtUsernameAndPasswordAuthenticationFilter(UserSessionService userSessionService, AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey) {
        setFilterProcessesUrl("/v1/login");
        this.userSessionService = userSessionService;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @SneakyThrows
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


            return authenticationManager.authenticate(authentication);

        } catch (IOException | AuthenticationException e) {
            if(e instanceof AuthenticationException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ((AuthenticationException) e).getMessage());
                return null;
            } else throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        final String username = authResult.getName();
        final Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        final java.sql.Date expiration = java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays()).toString());

        final String token = Jwts.builder()
            .setSubject(username)
            .claim("authorities", authorities)
            .setIssuedAt(new Date())
            .setExpiration(expiration)
            .signWith(secretKey)
            .compact();
        final String userInfo = Jwts.builder()
                .setSubject(username)
                .claim("authorities" ,authorities)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .compact();

        this.userSessionService.insertSession(
                new AppSession(username , token));

        final Cookie jwtTokenCookie = new Cookie(jwtConfig.getAuthorizationHeader() ,  token );
        jwtTokenCookie.setHttpOnly(true);
        final int maxAge = jwtConfig.getTokenExpirationAfterDays() * 86400;  //24 * 60 * 60
        jwtTokenCookie.setMaxAge(maxAge);
        jwtTokenCookie.setPath("/");
        response.addCookie(jwtTokenCookie);
        final Cookie userInfoCookie =  new Cookie("user_info", userInfo);
        userInfoCookie.setMaxAge(maxAge);
        userInfoCookie.setPath("/");
        response.addCookie(userInfoCookie);


    }




}
