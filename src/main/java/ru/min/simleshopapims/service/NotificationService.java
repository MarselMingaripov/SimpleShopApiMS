package ru.min.simleshopapims.service;

import ru.min.simleshopapims.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(Notification notification);

    List<Notification> showAllNotificationsByUsername(String username);

    List<Notification> sendNotification(Notification notification);
}
