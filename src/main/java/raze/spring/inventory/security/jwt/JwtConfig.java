package raze.spring.inventory.security.jwt;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtConfig {

    @Value("#{systemEnvironment['JWT_SECRET_KEY'] ?: 'qwekmq@WEqdoqOPDMAO_WUneqw22313MNoidJMNSDk2312331oidwoiq'}")
    private  String secretKey;
    @Value("#{systemEnvironment['JWT_TOKEN_PREFIX'] ?: 'Bearer'}")
    private  String tokenPrefix;
    @Value("#{systemEnvironment['JWT_TOKEN_EXPIRE'] ?: '10'}")
    private Integer tokenExpirationAfterDays;



    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
