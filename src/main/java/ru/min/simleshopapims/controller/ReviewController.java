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
import ru.min.simleshopapims.model.Review;
import ru.min.simleshopapims.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping(path = "/review")
@Tag(name = "API для работы с отзывами")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "создать отзыв. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв успешно создан"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей отзыва")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Review> createReview(@RequestBody Review review){
        return ResponseEntity.ok().body(reviewService.createReview(review));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "удалить отзыв по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв успешно удален"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Review> deleteReview(@PathVariable(required = true) Long id){
        reviewService.deleteReviewById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить отзыв по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей отзыва")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Review> updateReview(@RequestBody Review review, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(reviewService.updateReview(review, id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить список отзывов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзывы успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Review>> getAllReviews(){
        return ResponseEntity.ok().body(reviewService.findAll());
    }
}
