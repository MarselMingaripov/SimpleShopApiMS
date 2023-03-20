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
import ru.min.simleshopapims.model.dto.ProductDto;
import ru.min.simleshopapims.service.ProductService;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
@Tag(name = "API для работы с продуктами")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "создать продукт с активным статусом. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно создан"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей продукта")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto product){
        return ResponseEntity.ok().body(productService.createProduct(product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "удалить продукт по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно удален"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> deleteProduct(@PathVariable(required = true) Long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить продукт по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей продукта")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(productService.updateProduct(product, id));
    }

    @GetMapping("/all-with-AS")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить список продуктов с активным статусом с учетом скидки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукты успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> getAllProductsWithAS(){
        return ResponseEntity.ok().body(productService.findAllWithAS());
    }

    @GetMapping("/all-without-discount")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список всех продуктов без учета скидки. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукты успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> getAllProductsWithoutDiscount(){
        return ResponseEntity.ok().body(productService.findAll());
    }

    @GetMapping("/all-with-discount")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список всех продуктов с учетом скидки. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Нет скидок на продукты")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> getAllProductsWithDiscount(){
        return ResponseEntity.ok().body(productService.findAllWithDiscount());
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить продукт по названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно получен"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> findProductByName(@PathVariable String name){
        return ResponseEntity.ok().body(productService.findByName(name));
    }

    @PostMapping("/apply-to-create-product")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Подать заявку на добавление продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно получен"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей продукта"),
            @ApiResponse(responseCode = "409", description = "Вы можете добавлять продукты только в свои организации!")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> applyToCreateProduct(@RequestBody Product product){
        return ResponseEntity.ok().body(productService.applyToCreateProduct(product));
    }

    @PostMapping("/set-active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Сделать статус продукта активным. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно получил активный статус"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден"),
            @ApiResponse(responseCode = "409", description = "Организация не найдена по имени"),
            @ApiResponse(responseCode = "400", description = "Организация не имеет статуса активной")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> setActiveStatusToProduct(@RequestBody Product product, @RequestParam(required = true) String orgName ){
        return ResponseEntity.ok().body(productService.setActiveStatus(orgName, product));
    }

    @PostMapping("/set-frozen")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Сделать статус продукта замороженным. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно получил замороженный статус"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден"),
            @ApiResponse(responseCode = "409", description = "Организация не найдена по имени")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> setFrozenStatusToProduct(@RequestBody Product product, @RequestParam(required = true) String orgName ){
        return ResponseEntity.ok().body(productService.setFrozenStatus(orgName, product));
    }

    @PostMapping("/update-own-product/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Обновить данные о продукте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно обновлен"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей продукта"),
            @ApiResponse(responseCode = "409", description = "Организация пользователя не имеет такого продукта")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> updateOwnProduct(@RequestBody Product product, @PathVariable(required = true) Long id ){
        return ResponseEntity.ok().body(productService.updateOwnProduct(product, id));
    }

    @PostMapping("/set-organization")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Установить организацию. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организация успешно установлена"),
            @ApiResponse(responseCode = "404", description = "Организация или продукт не найдены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Product> setOrg(@RequestParam(required = true) Long prId, @RequestParam(required = true) Long orgId){
        return ResponseEntity.ok().body(productService.setOrganization(prId, orgId));
    }
}
