package raze.spring.inventory.service;

import org.springframework.data.domain.Page;
import raze.spring.inventory.domain.Activity;
import raze.spring.inventory.domain.dto.ActivityDto;
import raze.spring.inventory.security.model.UserAccount;

import java.util.List;

public interface ActivityService {
    Activity save(Activity activity);

    Activity findFirst();

    Activity findLast(UserAccount user);

    Page<Activity> findByUser(UserAccount user, int page, int size);

    Page<ActivityDto> getUserActivities(String username , int page, int size , String sort ,String search);

    Page<ActivityDto> getActivityPage(int page, int size , String sort ,String search);

    Activity findOne(long id);

    List<Activity> findAll();

    void delete(Long id);
}
