package raze.spring.inventory.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.security.model.UserSession;


@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,String> {

}
