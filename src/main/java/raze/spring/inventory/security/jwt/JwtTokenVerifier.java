package raze.spring.inventory.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import raze.spring.inventory.Exception.IllegalTokenException;
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
import java.util.stream.Stream;

@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final UserSessionService userSessionService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;


    private final OrRequestMatcher orRequestMatcher;




    public JwtTokenVerifier(UserSessionService userSessionService, SecretKey secretKey, JwtConfig jwtConfig) {
        this.userSessionService = userSessionService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;

        List<RequestMatcher> matchers = Stream.of( "/v1/forgotpassword")
        .map(AntPathRequestMatcher::new)
        .collect(Collectors.toList());
        this.orRequestMatcher = new OrRequestMatcher(matchers);
    }




//    @SneakyThrows(value = IllegalTokenException.class)
//    @SneakyThrows(IllegalTokenException.class)
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {



        final Cookie token = WebUtils.getCookie(request, jwtConfig.getAuthorizationHeader());





        if (token == null || Strings.isNullOrEmpty(token.getValue())  ) {
            filterChain.doFilter(request, response);
            return;
        }


        try {
            final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            Claims body = (Claims) jwtParser.parse(token.getValue()).getBody();
            String username = body.getSubject();

            if(!this.userSessionService.isSessionValid(username, token.getValue()) ) {
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
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("Token %s cannot be trusted", token.getValue()));
//            throw new IllegalTokenException(String.format("Token %s cannot be trusted", token));
            return;
        }

            filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return  orRequestMatcher.matches(request);
    }






}
