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

        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        final String authorizationCookie =
            Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("Authorization"))
                .findAny().get().getValue();



//        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        if (Strings.isNullOrEmpty(authorizationCookie) ) {
            filterChain.doFilter(request, response);
            return;
        }

//        String token = authorizationHeader.substring(7);
        String token = authorizationCookie;

        try {

//            Jws<Claims> claimsJws = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token);
//
//            Claims body = claimsJws.getBody();
//
//            String username = body.getSubject();

            final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            Claims cookieBody = (Claims) jwtParser.parse(authorizationCookie).getBody();
            Claims body = (Claims) jwtParser.parse(authorizationCookie).getBody();
            String username = body.getSubject();
            log.debug("AUTHORIZATION COOKIE : {}" , cookieBody);

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
