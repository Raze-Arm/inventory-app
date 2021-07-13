package raze.spring.inventory.security.bruteforce;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import raze.spring.inventory.registration.Request;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class ConfirmationTokenSenderAspect {

    private final ForgotPasswordAttemptService attemptService;



    @Before("execution(* raze.spring.inventory.registration.RegistrationService.forgotPassword(..)) && args(request, servletRequest)  ")
    public void beforeSendConfirmationToken(JoinPoint joinPoint, Request request,  HttpServletRequest servletRequest) {
        final String xfHeader = servletRequest.getHeader("X-Forwarded-For");
        log.debug("beforeSendConfirmationToken :{} ", request.getUsername());
        if(xfHeader == null) {
            attemptService.sentEmail(servletRequest.getRemoteAddr());
        } else {
            attemptService.sentEmail(xfHeader.split(",")[0]);
        }
    }
}

