package raze.spring.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.UserProfile;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
      @Query(
              value =
                      "SELECT P FROM UserProfile P WHERE P.account.username = :username")
      Optional<UserProfile> findByAccountUsername(String username);

      @Query(
          value =
              "SELECT P FROM UserProfile P WHERE P.firstName LIKE %:search% OR P.lastName LIKE %:search% OR P.account.username LIKE %:search%")
      Page<UserProfile> findAll(Pageable pageable, @Param("search") String search);
}
