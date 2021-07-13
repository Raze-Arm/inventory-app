package raze.spring.inventory.redis;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.security.model.AppSession;
import raze.spring.inventory.security.service.UserSessionService;

import javax.crypto.SecretKey;

@Service()
@Profile("redis")
public class RedisUserSessionServiceImpl implements UserSessionService {
    private final RedisUserSessionRepository userSessionRepository;
    private final SecretKey secretKey;

    public RedisUserSessionServiceImpl(RedisUserSessionRepository userSessionRepository, SecretKey secretKey) {
        this.userSessionRepository = userSessionRepository;
        this.secretKey = secretKey;
    }

    @Override
    public AppSession findSessionByUsername(String username) throws UnauthorizedException {
        return this.userSessionRepository.findById(username).orElseThrow(new UnauthorizedException("Username Not Found"));

    }

    @Override
    public boolean isSessionValid(String username, String token) throws UnauthorizedException {
        if(username == null || token == null )return false;

        final RedisUserSession userSession = this.userSessionRepository.findById(username).orElseThrow(new UnauthorizedException("Username Not Found"));
        if(userSession.getToken().equals(token)) return true;
        else return false;
    }

    @Override
    public boolean isTokenValid(String token) throws UnauthorizedException {

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
            this.userSessionRepository.save(RedisUserSession.builder().id(userSession.getUsername()).token(userSession.getToken()).build());
        }
    }

    @Override
    public void removeSession(String username) {
        if (username != null) this.userSessionRepository.deleteById(username);

    }

    @Override
    public void removeToken(String token) {
        final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        final Claims body = (Claims)jwtParser.parse(token).getBody();
        if(token != null) this.userSessionRepository.deleteById(body.getSubject());

    }
}
