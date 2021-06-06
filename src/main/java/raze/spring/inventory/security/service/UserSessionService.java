package raze.spring.inventory.security.service;


import raze.spring.inventory.Exception.ObjectNotFoundException;
import raze.spring.inventory.security.model.UserSession;

public interface UserSessionService {

    UserSession findSessionByUsername(String username) throws ObjectNotFoundException;
    boolean isSessionValid(String username,String token) throws ObjectNotFoundException;
    boolean  isTokenValid(String token) throws ObjectNotFoundException;
    void insertSession(UserSession userSession);
    void removeSession(String username);
    void removeToken(String token);
}
