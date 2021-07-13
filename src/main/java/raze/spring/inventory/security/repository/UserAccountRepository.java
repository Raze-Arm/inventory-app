package raze.spring.inventory.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import raze.spring.inventory.security.model.UserAccount;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {
    Optional<UserAccount> findByUsername(String username);



    @Transactional
    @Modifying
    @Query("UPDATE UserAccount u SET u.isEnabled = TRUE WHERE u.username = ?1")
    int enableUser(String username);


    @Transactional
    @Modifying
    @Query("UPDATE UserAccount u SET u.isEnabled = FALSE WHERE u.username = ?1")
    int disableUser(String username);
}
