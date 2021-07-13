package raze.spring.inventory.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);


    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.confirmAt = ?2 WHERE c.token = ?1")
    int updateConfirmedAt(String token, Timestamp createdAt);



}
