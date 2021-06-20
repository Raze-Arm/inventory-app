package raze.spring.inventory.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.security.service.UserSessionService;


import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final UserSessionService userSessionService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(UserSessionService userSessionService, SecretKey secretKey, JwtConfig jwtConfig) {
        this.userSessionService = userSessionService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        final Cookie[] cookies = request.getCookies();
        final String token =
            Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(jwtConfig.getAuthorizationHeader()))
                .findAny().get().getValue();





        if (Strings.isNullOrEmpty(token) ) {
            filterChain.doFilter(request, response);
            return;
        }


        try {
            final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            Claims cookieBody = (Claims) jwtParser.parse(token).getBody();
            Claims body = (Claims) jwtParser.parse(token).getBody();
            String username = body.getSubject();

            if(!this.userSessionService.isSessionValid(username,token) ) {
                filterChain.doFilter(request, response);
                return;
            }

            var authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);


        } catch (JwtException | NoSuchElementException | UnauthorizedException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        filterChain.doFilter(request, response);
    }
}
