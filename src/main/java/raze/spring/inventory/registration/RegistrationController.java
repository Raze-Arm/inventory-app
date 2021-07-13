package raze.spring.inventory.registration;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import raze.spring.inventory.Exception.RegistrationException;
import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.security.model.UserAccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;


@Slf4j
@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @PostMapping("/v1/forgotpassword")
    public ResponseEntity<Void> forgotPassword(@RequestBody Request request, HttpServletRequest servletRequest) throws UnsupportedEncodingException, RegistrationException, UnauthorizedException {
          registrationService.forgotPassword(request, servletRequest);
          return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = "/usersecurity/confirm")
    public ModelAndView confirm(
            @RequestParam("username") String username
            , @RequestParam("email") String email
            , @RequestParam("token") String token) throws RegistrationException {
         final UserAccount  userAccount = registrationService.confirmToken( username, email, token);
        ModelAndView modelAndView = new ModelAndView();

        if(userAccount != null) {
             authWithoutPassword(userAccount);
            final CustomUsernamePasswordAuthenticationToken authentication = (CustomUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            modelAndView.addObject("expiresAt", authentication.getExpiresAt());
            modelAndView.addObject("request", new ResetPasswordRequest());
             modelAndView.setViewName("forgot-password");
         } else {
            modelAndView.addObject("message", "Invalid token");
          modelAndView.setViewName("error");
        }
        return modelAndView;
    }




    @PostMapping(path = "/usersecurity/resetpassword")
    public ModelAndView reset(@Validated @ModelAttribute("request")  ResetPasswordRequest request
            , BindingResult bindingResult, HttpSession session) throws RegistrationException {
        final CustomUsernamePasswordAuthenticationToken authentication = (CustomUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if(Instant.now().isAfter(Instant.ofEpochSecond(authentication.getExpiresAt()))){
            throw new RegistrationException("Registration time expired, please try again");
        }
        final ModelAndView modelAndView = new ModelAndView();
        if(bindingResult.hasErrors()) {
            modelAndView.addObject(bindingResult.getModel());
            modelAndView.addObject("expiresAt", authentication.getExpiresAt());
            modelAndView.setViewName("forgot-password");
        }else {

             registrationService.resetUserPassword(authentication.getName(), request.getPassword());
            logoutUser(session);


            modelAndView.setViewName("success");

        }

        return modelAndView;

    }

    private void logoutUser(HttpSession session) {
        final SecurityContext context = SecurityContextHolder.getContext();
        session.removeAttribute("expiresAt");
        session.removeAttribute("request");
        context.setAuthentication(null);
        session.invalidate();
    }


    public void authWithoutPassword(UserAccount user) {
        final SecurityContext context = SecurityContextHolder.getContext();

        CustomUsernamePasswordAuthenticationToken authentication = new CustomUsernamePasswordAuthenticationToken(user, null
                    , user.getUserRoles().getGrantedAuthorities());

            context.setAuthentication(authentication);
    }

    @ExceptionHandler(RegistrationException.class)
    public ModelAndView handleRegistrationException(RegistrationException e) {
        return new ModelAndView("error", Map.of("message", e.getMessage()));
    }

}


@Getter
@Setter
class  CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken  {
    private long expiresAt;

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.expiresAt = Instant.now().getEpochSecond() + 600;
    }


}
