package ru.min.simleshopapims.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.min.simleshopapims.model.Notification;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping(path = "/notification")
@Tag(name = "API для работы с уведомлениями")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "создать уведомление и отправить пользователю. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомление успешно создано"),
            @ApiResponse(responseCode = "404", description = "Получатель не найден"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей уведомления")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification){
        return ResponseEntity.ok().body(notificationService.createNotification(notification));
    }

    @GetMapping("/all/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить все уведомления конкретного пользователя. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомления успешно получены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Notification>> showNotificationsByUser(@PathVariable String username){
        return ResponseEntity.ok().body(notificationService.showAllNotificationsByUsername(username));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить все уведомления. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомления успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Notification>> showNotifications(){
        return ResponseEntity.ok().body(notificationService.showAllNotifications());
    }

    @GetMapping("/allOwn")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить все уведомления текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомления успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Notification>> showNotificationsOwn(){
        return ResponseEntity.ok().body(notificationService.showOwnNotifications());
    }
}
