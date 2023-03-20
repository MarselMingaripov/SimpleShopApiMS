package ru.min.simleshopapims.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String header;
    private LocalDate localDate;
    private String description;
    private String recipient;
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    public Notification(String header, LocalDate localDate, String description, String recipient) {
        this.header = header;
        this.localDate = LocalDate.now();
        this.description = description;
        this.recipient = recipient;
        this.notificationStatus = NotificationStatus.NOT_READ;
    }
}
