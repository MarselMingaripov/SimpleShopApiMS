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
import ru.min.simleshopapims.model.Characteristic;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.service.CharacteristicService;

import java.util.List;

@RestController
@RequestMapping(path = "/characteristic")
@Tag(name = "API для работы с характеристиками продуктов. Только для админа")
@RequiredArgsConstructor
public class CharacteristicController {

    private final CharacteristicService characteristicService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "создать характеристику. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Характеристика создана и сохранена"),
            @ApiResponse(responseCode = "409", description = "Характеристика с таким именем уже есть"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей характеристики")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Characteristic> createCharacteristic(@RequestBody Characteristic characteristic){
        return ResponseEntity.ok().body(characteristicService.createCharacteristic(characteristic));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "удалить характеристику по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Характериииистика успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Характеристика не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Characteristic> deleteCharacteristic(@PathVariable(required = true) Long id){
        characteristicService.deleteCharacteristicById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить характеристику по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно удален"),
            @ApiResponse(responseCode = "404", description = "Характеристика не найдена"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей характеристики")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Characteristic> updateCharacteristic(@RequestBody Characteristic characteristic, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(characteristicService.updateCharacteristic(characteristic, id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список характеристик")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Характеристики успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Characteristic>> getAllCharacteristics(){
        return ResponseEntity.ok().body(characteristicService.findAll());
    }
}
