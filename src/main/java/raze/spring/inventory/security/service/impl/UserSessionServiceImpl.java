package raze.spring.inventory.security.service.impl;

import io.jsonwebtoken.*;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raze.spring.inventory.Exception.ObjectNotFoundException;
import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.security.model.AppSession;
import raze.spring.inventory.security.model.UserSession;
import raze.spring.inventory.security.repository.UserSessionRepository;
import raze.spring.inventory.security.service.UserSessionService;


import javax.crypto.SecretKey;
import javax.transaction.Transactional;

@Service
@Profile("!redis")
public class UserSessionServiceImpl implements UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final SecretKey secretKey;

    public UserSessionServiceImpl(UserSessionRepository userSessionRepository, SecretKey secretKey) {
        this.userSessionRepository = userSessionRepository;
        this.secretKey = secretKey;
    }

    @Override
    public UserSession findSessionByUsername(String username) throws UnauthorizedException {
        return this.userSessionRepository.findById(username).orElseThrow(new UnauthorizedException("Username Not Found"));
    }

    @Override
    public boolean isSessionValid(String username, String token) throws  UnauthorizedException {
        if(username == null || token == null )return false;

        final UserSession userSession = this.userSessionRepository.findById(username).orElseThrow(new UnauthorizedException("Username Not Found"));
        if(userSession.getToken().equals(token)) return true;
        else return false;
    }

    @Override
    public boolean isTokenValid(String token) throws  UnauthorizedException {
//        String tokenSlice = token.substring(7);

        Jws<Claims> claimsJws =
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        Claims body = claimsJws.getBody();

        String username = body.getSubject();

        if (!this.isSessionValid(username, token)) {
            return false;
        }
        return true;

    }

    @Override
    public void insertSession(AppSession userSession) {
        if (userSession != null) {
//            this.userSessionRepository.findById(userSession.getUsername()).ifPresent(sessionUser1 -> this.sessionUserRepository.deleteById(sessionUser1.getUsername()));
            this.userSessionRepository.save(new UserSession(userSession.getUsername(), userSession.getToken()));
        }
    }

    @Override
    public void removeSession(String username) {
        if (username != null) this.userSessionRepository.deleteById(username);
    }

    @Transactional
    @Override
    public void removeToken(String token) {
        if(token != null) this.userSessionRepository.deleteByToken(token);
    }
}
