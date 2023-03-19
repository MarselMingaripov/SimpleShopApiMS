package ru.min.simleshopapims.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.service.BasketService;

import java.util.List;

@RestController
@RequestMapping(path = "/basket")
@Tag(name = "API для работы с корзиной")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Добавить продукт в корзину")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно добавлен")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Product>> addToBasket(@RequestBody Product product){
        return ResponseEntity.ok().body(basketService.addToBasket(product));
    }
}
