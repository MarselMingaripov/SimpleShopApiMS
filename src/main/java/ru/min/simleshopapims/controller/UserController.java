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
import ru.min.simleshopapims.model.Purchase;
import ru.min.simleshopapims.security.model.User;
import ru.min.simleshopapims.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
@Tag(name = "API для работы пользователя с продуктами")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/set-balance/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Пополнить счет пользователя по имени. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно создан"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким именем не найден")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Double> setBalance(@PathVariable String username, @RequestParam Double amount){
        return ResponseEntity.ok().body(userService.setBalance(username, amount));
    }

    @GetMapping("/current-user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден успешно создан")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<User> getCurrentUser(){
        return ResponseEntity.ok().body(userService.getCurrentUser());
    }

    @PostMapping("/buy")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Совершить покупку. Предварительно нужно поместить товары в корзину")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупка успешна"),
            @ApiResponse(responseCode = "411", description = "Корзина пуста"),
            @ApiResponse(responseCode = "413", description = "На балансе не хватает средств")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Purchase> buy(){
        return ResponseEntity.ok().body(userService.buy());
    }

    @PostMapping("/return")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Возврат покупки. Возможен в течении суток с момента совершения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупка успешна возвращена"),
            @ApiResponse(responseCode = "409", description = "Нельзя вернуть то, что вы не покупали"),
            @ApiResponse(responseCode = "410", description = "Отклонено. Возврат возможен в течении суток")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Purchase> returnProduct(Product product){
        return ResponseEntity.ok().body(userService.purchaseReturns(product));
    }

    @PostMapping("/put-grade/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Поставить оценку товару")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оценка успешна поставлена"),
            @ApiResponse(responseCode = "409", description = "Товар не найден"),
            @ApiResponse(responseCode = "411", description = "Нельзя оценить то, что вы не покупали")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> putGrade(@PathVariable Long id, @RequestParam int grade){
        return ResponseEntity.ok().body(userService.putGrade(id, grade));
    }

    @PostMapping("/put-review/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Поставить отзыв товару")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв успешна поставлен"),
            @ApiResponse(responseCode = "409", description = "Товар не найден"),
            @ApiResponse(responseCode = "411", description = "Нельзя оставить отзыв на то, что вы не покупали")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> putReview(@PathVariable Long id, @RequestParam String name, @RequestBody String review){
        return ResponseEntity.ok().body(userService.putReview(id,name, review));
    }

    @GetMapping("/get-all-purchases-current-user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить список покупок текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв успешна поставлен")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Purchase>> getPurchases(){
        return ResponseEntity.ok().body(userService.findAllByCurrentUser());
    }

    @GetMapping("/get-all-notifications")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить список уведомлений текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомления успешна получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Notification>> getNotifications(){
        return ResponseEntity.ok().body(userService.showAllNotifications());
    }

    @PostMapping("/earn-money")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Вывести деньги со счетов своих организаций на свой баланс")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Деньги успешно выведены"),
            @ApiResponse(responseCode = "404", description = "Организации пользователя не найдены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Double> earnMoney(){
        return ResponseEntity.ok().body(userService.addManyFromOrgToBalance());
    }
}
