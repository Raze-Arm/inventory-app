package raze.spring.inventory.registration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import raze.spring.inventory.Exception.RegistrationException;
import raze.spring.inventory.Exception.UnauthorizedException;
import raze.spring.inventory.domain.UserProfile;
import raze.spring.inventory.registration.email.EmailService;
import raze.spring.inventory.registration.token.ConfirmationToken;
import raze.spring.inventory.registration.token.ConfirmationTokenService;
import raze.spring.inventory.repository.UserProfileRepository;
import raze.spring.inventory.security.bruteforce.ForgotPasswordAttemptService;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.security.service.UserAccountService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserAccountService userAccountService;
    private final RegistrationProperties registrationProperties;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final UserProfileRepository userProfileRepository;

    private final ForgotPasswordAttemptService attemptService;


    @Transactional
    protected UserProfile addConfirmationTokenToUser(String username, String email, String token, Duration duration) throws RegistrationException {

        final Instant now = Instant.now();
        final UserProfile userProfile = userProfileRepository.findByAccountUsername(username)
                .orElseThrow(() -> new RegistrationException("username not found!"));
        if(!email.equals(userProfile.getEmail())) throw new IllegalStateException("invalid email");
        final ConfirmationToken confirmationToken
                = ConfirmationToken.builder().token(token).createdDate(Timestamp.from(now))
                .expiresAt(Timestamp.from(now.plus(duration)))
                .userProfile(userProfile)
                .build();
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return userProfile;
    }
    private static String encodeUtf8(String val) throws UnsupportedEncodingException {
        return URLEncoder.encode(val, StandardCharsets.UTF_8);
    }
//    @Transactional
    public void forgotPassword( Request request, HttpServletRequest servletRequest) throws UnsupportedEncodingException, RegistrationException, UnauthorizedException {
        final String ip = getClientIp(servletRequest);
        if(attemptService.isBlocked(ip)) {
            log.debug("BLOCKING IP : {}", ip);
            throw new UnauthorizedException("Your ip is temporarily blocked, Please try later");
        }
        final String username = request.getUsername();
        final String token = UUID.randomUUID().toString();
        final UserProfile userProfile = addConfirmationTokenToUser(username, request.getEmail(), token, Duration.ofMinutes(15));
        final String email = userProfile.getEmail();
        final String link = UriComponentsBuilder.fromUriString(registrationProperties.getActivationLink())
                .queryParam("username", encodeUtf8(username))
                .queryParam("email", encodeUtf8(email))
                .queryParam("token", encodeUtf8(token))
                .build()
                .toUriString();
        emailService.send(email, buildEmail(username, link) );
    }

    @Transactional
    public UserAccount confirmToken(String username , String email, String token) throws RegistrationException {
        final ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token).orElseThrow(() -> new RegistrationException("token not found"));
        if(confirmationToken.getConfirmAt() != null) {
            throw new RegistrationException("email already confirmed");
        }

        final Timestamp expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.before(Timestamp.from(Instant.now()))) {
            throw new RegistrationException("token expired");
        }
        final UserProfile userProfile = confirmationToken.getUserProfile();

        final UserAccount account = userProfile.getAccount();
        if(!userProfile.getEmail().equals(email) || !account.getUsername().equals(username) ) throw new RegistrationException("invalid email");

        confirmationTokenService.setConfirmedAt(token);
        return account;

    }

    public void resetUserPassword(String username, String  password) {
        userAccountService.changePassword(username, password);
    }

    private String getClientIp(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if(xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
    private String buildEmail(String name, String link) {
    return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c;direction:rtl\">\n"
        + "\n"
        + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n"
        + "\n"
        + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
        + "    <tbody><tr>\n"
        + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n"
        + "        \n"
        + "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
        + "          <tbody><tr>\n"
        + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
        + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
        + "                  <tbody><tr>\n"
        + "                    <td style=\"padding-left:10px\">\n"
        + "                  \n"
        + "                    </td>\n"
        + "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
        + "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">ایمیل خود را تایید کنید\n</span>\n"
        + "                    </td>\n"
        + "                  </tr>\n"
        + "                </tbody></table>\n"
        + "              </a>\n"
        + "            </td>\n"
        + "          </tr>\n"
        + "        </tbody></table>\n"
        + "        \n"
        + "      </td>\n"
        + "    </tr>\n"
        + "  </tbody></table>\n"
        + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
        + "    <tbody><tr>\n"
        + "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
        + "      <td>\n"
        + "        \n"
        + "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
        + "                  <tbody><tr>\n"
        + "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
        + "                  </tr>\n"
        + "                </tbody></table>\n"
        + "        \n"
        + "      </td>\n"
        + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n"
        + "    </tr>\n"
        + "  </tbody></table>\n"
        + "\n"
        + "\n"
        + "\n"
        + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
        + "    <tbody><tr>\n"
        + "      <td height=\"30\"><br></td>\n"
        + "    </tr>\n"
        + "    <tr>\n"
        + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
        + "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
        + "        \n"
        + "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">سلام "
        + name
        + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">لطفا برای تغییر رمز عبور خود بر روی لینک زیر کلیک کنید: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
        + link
        + "\">هم اکنون تغییر بده\n</a> </p></blockquote>\nپیوند 15 دقیقه دیگر منقضی می شود. <p>\n</p>"
        + "        \n"
        + "      </td>\n"
        + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
        + "    </tr>\n"
        + "    <tr>\n"
        + "      <td height=\"30\"><br></td>\n"
        + "    </tr>\n"
        + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n"
        + "\n"
        + "</div></div>";
    }
}
