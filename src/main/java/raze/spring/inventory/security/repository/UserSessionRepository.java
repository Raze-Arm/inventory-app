package raze.spring.inventory.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.security.model.UserSession;

import javax.transaction.Transactional;


@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,String> {
    @Modifying
    @Query(value = "DELETE FROM UserSession U    WHERE U.token = :token" )
    void deleteByToken(@Param("token") String token);

}
