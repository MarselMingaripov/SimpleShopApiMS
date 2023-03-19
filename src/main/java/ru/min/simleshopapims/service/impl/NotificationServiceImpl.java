package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Notification;
import ru.min.simleshopapims.repository.NotificationRepository;
import ru.min.simleshopapims.security.model.User;
import ru.min.simleshopapims.security.repository.UserRepository;
import ru.min.simleshopapims.service.NotificationService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ValidationService validationService;
    private final UserRepository userRepository;

    @Override
    public Notification createNotification(Notification notification) {
        if (validationService.validateNotification(notification)) {
            if (userRepository.existsByUsername(notification.getRecipient())) {
                return notificationRepository.save(sendNotification(notification));
            } else {
                throw new DontExistsByNameException("User not found!");
            }
        } else {
            throw new MyValidationException("Notification has invalid fields!");
        }
    }

    @Override
    public List<Notification> showAllNotificationsByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            return notificationRepository.findAllByRecipient(username);
        } else {
            throw new DontExistsByNameException("User not found!");
        }
    }

    @Override
    public List<Notification> showAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> showOwnNotifications(){
        return userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .getNotifications();
    }

    @Override
    public Notification sendNotification(Notification notification) {
        User user = userRepository.findUserByUsername(notification.getRecipient());
        List<Notification> userNotifications = user.getNotifications();
        userNotifications.add(notification);
        user.setNotifications(userNotifications);
        return notification;
    }
}
