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
import ru.min.simleshopapims.model.Grade;
import ru.min.simleshopapims.model.KeyWord;
import ru.min.simleshopapims.service.KeyWordService;

import java.util.List;

@RestController
@RequestMapping(path = "/keyWord")
@Tag(name = "API для работы с ключевыми словами")
@RequiredArgsConstructor
public class KeyWorldController {

    private final KeyWordService keyWordService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "создать ключевое слово")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ключевое слово успешно создано"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей ключевого слова")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<KeyWord> createKeyWord(@RequestBody KeyWord keyWord){
        return ResponseEntity.ok().body(keyWordService.createKeyWord(keyWord));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "удалить ключевое слово по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ключевое слово успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Ключевое слово не найдено")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<KeyWord> deleteKeyWord(@PathVariable(required = true) Long id){
        keyWordService.deleteKeyWordById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить ключевое слово по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ключевое слово успешно обновлено"),
            @ApiResponse(responseCode = "404", description = "Кдючевое слово не найдено"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей ключевого слова")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<KeyWord> updateKeyWord(@RequestBody KeyWord keyWord, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(keyWordService.updateKeyWord(keyWord, id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список ключевых слов. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ключевые слова успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<KeyWord>> getAllKeyWords(){
        return ResponseEntity.ok().body(keyWordService.findAll());
    }
}
