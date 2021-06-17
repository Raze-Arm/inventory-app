package raze.spring.inventory.security.service;


import raze.spring.inventory.Exception.ObjectNotFoundException;
import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.security.model.UserSession;

public interface UserSessionService {

    UserSession findSessionByUsername(String username) throws  UnauthorizedException;
    boolean isSessionValid(String username,String token) throws  UnauthorizedException;
    boolean  isTokenValid(String token) throws  UnauthorizedException;
    void insertSession(UserSession userSession);
    void removeSession(String username);
    void removeToken(String token);
}
