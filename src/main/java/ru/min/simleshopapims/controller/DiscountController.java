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
import ru.min.simleshopapims.model.dto.DiscountDto;
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
    public ResponseEntity<Discount> createDiscount(@RequestBody DiscountDto discountDto){
        return ResponseEntity.ok().body(discountService.createDiscount(discountDto));
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
    public ResponseEntity<Discount> updateDiscount(@RequestBody DiscountDto discountDto, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.updateDiscount(discountDto, id));
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

    @PostMapping("/set-discount-to-list/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Найти скидку по ИД и установить для списка продуктов. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
            @ApiResponse(responseCode = "409", description = "Некоторые продукты в списке не удалось найти")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> setDiscountForListOfProductsByAdmin(@RequestBody List<Long> productsId, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.setDiscountToListOfProducts(productsId, id));
    }

    @PostMapping("/set-discount-to-list-by-user/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Найти скидку по ИД и установить для списка своих продуктов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
            @ApiResponse(responseCode = "409", description = "Некоторые продукты в списке не удалось найти")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> setDiscountForListOfProductsByUser(@RequestBody List<Long> productsId, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.setDiscountToListOfOwnProducts(productsId, id));
    }

    @PostMapping("/set-discount/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Найти скидку по ИД и установить для одного продукта. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
            @ApiResponse(responseCode = "409", description = "Некоторые продукты в списке не удалось найти")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> setDiscountForProductByAdmin(@RequestParam Long productId, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.setDiscountToProduct(productId, id));
    }

    @PostMapping("/set-discount-by-user/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Найти скидку по ИД и установить для одного своего продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидка успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
            @ApiResponse(responseCode = "409", description = "Некоторые продукты в списке не удалось найти")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> setDiscountForProductByUser(@RequestParam Long productId, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(discountService.setDiscountToOwnProduct(productId, id));
    }

    @GetMapping("/all-products-by-discount-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список продуктов по ид скидки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скидки успешно получены"),
            @ApiResponse(responseCode = "404", description = "Скидка не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> getAllProductsById(@PathVariable Long id){
        return ResponseEntity.ok().body(discountService.showListOfProductsByDiscountId(id));
    }
}
