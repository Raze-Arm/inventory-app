package raze.spring.inventory.security.service;


import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.security.model.AppSession;

public interface UserSessionService {

    AppSession findSessionByUsername(String username) throws  UnauthorizedException;
    boolean isSessionValid(String username,String token) throws  UnauthorizedException;
    boolean  isTokenValid(String token) throws  UnauthorizedException;
    void insertSession(AppSession userSession);
    void removeSession(String username);
    void removeToken(String token);
}
