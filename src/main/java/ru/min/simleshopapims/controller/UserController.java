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
import ru.min.simleshopapims.model.Purchase;
import ru.min.simleshopapims.security.model.User;
import ru.min.simleshopapims.service.UserService;

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
}
