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
import ru.min.simleshopapims.model.Discount;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.service.DiscountService;

import java.util.List;

@RestController
@RequestMapping(path = "/discount")
@Tag(name = "API для работы со скидками")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "создать скидку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно создан"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей скидки")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount){
        return ResponseEntity.ok().body(discountService.createDiscount(discount));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "удалить скидку по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найден")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Discount> deleteDiscount(@PathVariable(required = true) Long id){
        discountService.deleteDiscountById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить скидку по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найден"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей скидки")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Discount> updateDiscount(@RequestBody Discount discount, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.updateDiscount(discount, id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить список скидок")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидки успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Discount>> getAllDiscounts(){
        return ResponseEntity.ok().body(discountService.findAll());
    }

    @PostMapping("/setDiscountToList/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Найти скидку по ИД и установить для списка продуктов. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
            @ApiResponse(responseCode = "409", description = "Некоторые продукты в списке не удалось найти")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> setDiscountForListOfProductsByAdmin(@RequestBody List<Product> products, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.setDiscountToListOfProducts(products, id));
    }

    @PostMapping("/setDiscountToListByUser/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Найти скидку по ИД и установить для списка продуктов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
            @ApiResponse(responseCode = "409", description = "Некоторые продукты в списке не удалось найти")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> setDiscountForListOfProductsByUser(@RequestBody List<Product> products, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.setDiscountToListOfOwnProducts(products, id));
    }

    @PostMapping("/setDiscount/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Найти скидку по ИД и установить для одного продукта. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
            @ApiResponse(responseCode = "409", description = "Некоторые продукты в списке не удалось найти")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> setDiscountForProductByAdmin(@RequestBody Product product, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.setDiscountToProduct(product, id));
    }

    @PostMapping("/setDiscountByUser/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Найти скидку по ИД и установить для одного продукта. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
            @ApiResponse(responseCode = "409", description = "Некоторые продукты в списке не удалось найти")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> setDiscountForProductByUser(@RequestBody Product product, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.setDiscountToOwnProduct(product, id));
    }
}
