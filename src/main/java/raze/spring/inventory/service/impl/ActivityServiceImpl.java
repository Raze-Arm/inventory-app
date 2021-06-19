package raze.spring.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raze.spring.inventory.converter.ActivityToActivityDto;
import raze.spring.inventory.domain.Activity;
import raze.spring.inventory.domain.dto.ActivityDto;
import raze.spring.inventory.repository.ActivityRepository;
import raze.spring.inventory.security.model.UserAccount;
import raze.spring.inventory.service.ActivityService;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepo;
    private final ActivityToActivityDto activityToActivityDto;

    public ActivityServiceImpl(ActivityRepository activityRepo, ActivityToActivityDto activityToActivityDto) {
        this.activityRepo = activityRepo;
        this.activityToActivityDto = activityToActivityDto;
    }

    @Override
    public Activity save(Activity activity) {
        return this.activityRepo.save(activity);
    }

    @Override
    public Activity findFirst() {
        return this.activityRepo.findFirstBy();
    }

    @Override
    public Activity findLast(UserAccount user) {
        return activityRepo.findFirstByUserOrderByIdDesc(user);
    }

    @Override
    public Page<Activity> findByUser(UserAccount user, int page, int size) {
        return this.activityRepo.findByUser(user,  PageRequest.of(page, size, Sort.Direction.DESC));
    }

    @Override
    public Page<ActivityDto> getUserActivities(String username ,int page, int size, String sort, String search) {

        final Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC ,  sort != null ? sort : "created"));
        if(search == null || search.length() == 0) {
            return this.activityRepo.findAllByUsername(pageable, username).map(this.activityToActivityDto::convert);
        } else {
            return this.activityRepo.findAllByUsername(pageable, username, search).map(this.activityToActivityDto::convert);
        }
    }

    @Override
    public Page<ActivityDto> getActivityPage(int page, int size, String sort, String search) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC , sort != null ? sort : "created" ));
        if(search == null || search.length() == 0) {
            return this.activityRepo.findAll(pageable).map(this.activityToActivityDto::convert);
        } else {
            return this.activityRepo.findAll(pageable, search).map(this.activityToActivityDto::convert);
        }
    }

    @Override
    public Activity findOne(long id) {
        return this.activityRepo.findById(id).orElse(null);
    }

    @Override
    public List<Activity> findAll() {
        return this.activityRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        this.activityRepo.deleteById(id);
    }

}