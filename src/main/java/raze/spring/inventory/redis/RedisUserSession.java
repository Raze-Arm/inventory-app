package raze.spring.inventory.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import raze.spring.inventory.security.model.AppSession;

import java.io.Serializable;


@NoArgsConstructor
@Getter
@RedisHash("UserSession")
public class RedisUserSession extends AppSession implements Serializable {
    private String id;
    private  String token;

    @Builder
    public RedisUserSession(String id, String token) {
        super(id, token);
        this.id = id ;
        this.token = token;
    }
}
