package raze.spring.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raze.spring.inventory.domain.Activity;
import raze.spring.inventory.security.model.UserAccount;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {
    Activity findFirstBy();
    Activity findFirstByUserOrderByIdDesc(UserAccount user);
    Page<Activity> findByUser(UserAccount user, Pageable pageable);

    @Query(value = "SELECT A FROM Activity A WHERE A.entity LIKE %:search% OR A.requestMethod LIKE %:search% ")
    Page<Activity> findAll(Pageable pageable , @Param("search") String search);

    @Query(value = "SELECT A FROM Activity A WHERE A.user.username = :username  ")
    Page<Activity> findAllByUsername(Pageable pageable, @Param("username") String username);
    @Query(value = "SELECT A FROM Activity A WHERE A.user.username = :username and A.entity LIKE %:search% OR A.requestMethod LIKE %:search% ")
    Page<Activity> findAllByUsername(Pageable pageable,@Param("username") String username ,@Param("search") String search );
}