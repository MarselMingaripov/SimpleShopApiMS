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
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.model.Purchase;
import ru.min.simleshopapims.service.PurchaseService;

import java.util.List;

@RestController
@RequestMapping(path = "/purchase")
@Tag(name = "API для работы с покупками. Только для админа")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать покупку. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупка успешно создана")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase){
        return ResponseEntity.ok().body(purchaseService.createPurchase(purchase));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить покупку по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупка успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Покупка не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Purchase> deletePurchase(@PathVariable(required = true) Long id){
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить покупку по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупка успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Покупка не найден"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей покупки")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Purchase> updatePurchase(@RequestBody Purchase purchase, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(purchaseService.updatePurchase(purchase, id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список всех покупок")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупки успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Purchase>> getAllPurchases(){
        return ResponseEntity.ok().body(purchaseService.findAll());
    }

    @GetMapping("/all/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список покупок пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупки успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Purchase>> getAllPurchasesByUsername(@PathVariable String username){
        return ResponseEntity.ok().body(purchaseService.findAllByUsername(username));
    }

    @GetMapping("/all-own")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить список покупок текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупки успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Purchase>> getAllPurchasesByOwn(){
        return ResponseEntity.ok().body(purchaseService.findAllOwn());
    }
}
