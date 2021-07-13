package raze.spring.inventory.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisUserSessionRepository extends CrudRepository<RedisUserSession, String> {
}
