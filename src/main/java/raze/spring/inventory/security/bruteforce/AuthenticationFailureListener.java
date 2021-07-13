package raze.spring.inventory.security.bruteforce;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@AllArgsConstructor

public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;



    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
      final String xfHeader = request.getHeader("X-Forwarded-For");
      log.debug("Authentication failed : {}", request.getHeader("X-Forwarded-For"));
      if(xfHeader == null) {
          loginAttemptService.loginFailed(request.getRemoteAddr());
      } else {
          loginAttemptService.loginFailed(xfHeader.split(",")[0]);
      }


    }
}
