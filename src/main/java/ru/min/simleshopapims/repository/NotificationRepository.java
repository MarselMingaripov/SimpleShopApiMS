package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Notification;
import ru.min.simleshopapims.model.NotificationStatus;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByNotificationStatus(NotificationStatus notificationStatus);
    List<Notification> findAllByRecipient(String recipient);
}
