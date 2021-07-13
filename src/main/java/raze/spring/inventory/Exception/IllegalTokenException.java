package raze.spring.inventory.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED) //401
public class IllegalTokenException extends AuthenticationException {
    public IllegalTokenException(String s) {
        super(s);
    }
}
