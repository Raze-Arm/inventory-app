package raze.spring.inventory.converter;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raze.spring.inventory.domain.Activity;
import raze.spring.inventory.domain.dto.ActivityDto;
import raze.spring.inventory.utility.DateMapper;

@Component
public class ActivityToActivityDto implements Converter<Activity, ActivityDto> {
    private final DateMapper dateMapper;

    public ActivityToActivityDto(DateMapper dateMapper) {
        this.dateMapper = dateMapper;
    }

    @Synchronized
    @Override
    public ActivityDto convert(Activity activity) {
        return ActivityDto.builder()
                .id(activity.getId())
                .username(activity.getUser().getUsername())
                .method(activity.getRequestMethod())
                .entity(activity.getEntity())
                .createdDate(dateMapper.asOffsetDateTime(activity.getCreated()))
                .ip(activity.getIp())
                .parameter(activity.getParameter())
                .build();
    }
}
