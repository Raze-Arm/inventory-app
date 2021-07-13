package raze.spring.inventory.security.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.security.bruteforce.LoginAttemptService;
import raze.spring.inventory.security.service.UserAccountService;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Service("appUserDetailService")
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserAccountService userAccountService;
    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;


//    @SneakyThrows
//    @SneakyThrows(InternalAuthenticationServiceException.class)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final String ip = getClientIp();
        if(loginAttemptService.isBlocked(ip)) {
            log.debug("BLOCKING IP : {}", ip);
            throw new InternalAuthenticationServiceException("Your ip is temporarily blocked, Please try later");
        }

        try{
            return this.userAccountService.getUserByUsername(username);
        }catch (RuntimeException e ) {
            if (e instanceof AuthenticationException) throw e;
            throw new InternalAuthenticationServiceException("Error while executing loadUserByUsername", e);
        }
    }

    private String getClientIp() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if(xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
