package raze.spring.inventory.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import raze.spring.inventory.Exception.ObjectNotFoundException;
import raze.spring.inventory.security.model.UserSession;
import raze.spring.inventory.security.repository.UserSessionRepository;
import raze.spring.inventory.security.service.UserSessionService;


import javax.crypto.SecretKey;

@Service
public class UserSessionServiceImpl implements UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final SecretKey secretKey;

    public UserSessionServiceImpl(UserSessionRepository userSessionRepository, SecretKey secretKey) {
        this.userSessionRepository = userSessionRepository;
        this.secretKey = secretKey;
    }

    @Override
    public UserSession findSessionByUsername(String username) throws ObjectNotFoundException {
        return this.userSessionRepository.findById(username).orElseThrow(new ObjectNotFoundException("Username Not Found"));
    }

    @Override
    public boolean isSessionValid(String username, String token) throws ObjectNotFoundException {
        if(username == null || token == null )return false;

        final UserSession userSession = this.userSessionRepository.findById(username).orElseThrow(new ObjectNotFoundException("Username Not Found"));
        if(userSession.getToken().equals(token)) return true;
        else return false;
    }

    @Override
    public boolean isTokenValid(String token) throws ObjectNotFoundException {
        String tokenSlice = token.substring(7);



        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(tokenSlice);

        Claims body = claimsJws.getBody();

        String username = body.getSubject();

        if (!this.isSessionValid(username, tokenSlice)) {
            return false;
        }
        return true;

    }

    @Override
    public void insertSession(UserSession userSession) {
        if (userSession != null) {
//            this.userSessionRepository.findById(userSession.getUsername()).ifPresent(sessionUser1 -> this.sessionUserRepository.deleteById(sessionUser1.getUsername()));
            this.userSessionRepository.save(userSession);
        }
    }

    @Override
    public void removeSession(String username) {
        if (username != null) this.userSessionRepository.deleteById(username);
    }

    @Override
    public void removeToken(String token) {
        if(token != null) this.userSessionRepository.deleteByToken(token);
    }
}
