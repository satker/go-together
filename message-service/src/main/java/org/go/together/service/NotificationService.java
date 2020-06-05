package org.go.together.service;

import org.go.together.dto.NotificationDto;
import org.go.together.dto.filter.FieldMapper;
import org.go.together.logic.CrudService;
import org.go.together.mapper.NotificationMapper;
import org.go.together.model.Notification;
import org.go.together.repository.NotificationRepository;
import org.go.together.validation.NotificationValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService extends CrudService<NotificationDto, Notification> {
    protected NotificationService(NotificationRepository notificationRepository,
                                  NotificationMapper notificationMapper,
                                  NotificationValidator notificationValidator) {
        super(notificationRepository, notificationMapper, notificationValidator);
    }

    @Override
    public String getServiceName() {
        return "notification";
    }

    @Override
    public Map<String, FieldMapper> getMappingFields() {
        return null;
    }
}
