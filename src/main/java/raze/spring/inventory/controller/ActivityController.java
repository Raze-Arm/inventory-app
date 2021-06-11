package raze.spring.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import raze.spring.inventory.domain.dto.ActivityDto;
import raze.spring.inventory.service.ActivityService;

@RestController()
@RequestMapping("/v1")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping(path = {"/profile/activity", "/profile/activity/"})
    public ResponseEntity<Page<ActivityDto>> getUserActivityPage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search, Authentication authentication
            ) {
        return ResponseEntity.ok(
            this.activityService.getUserActivities(authentication.getName(), page, size, sort, search));
    }

    @GetMapping(path = {"/activity" ,"/activity/"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ActivityDto>> getActivityPage(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search
    ) {
        return  ResponseEntity.ok(this.activityService.getActivityPage(page, size, sort, search));
    }

    @GetMapping(path = {"/activity", "/activity/"}, params = {"username"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ActivityDto>> getActivityPageByUsername(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sort",required = false) String sort,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam("username") String username
    ) {
        return ResponseEntity.ok(
                this.activityService.getUserActivities(username, page, size, sort, search));
    }



}
