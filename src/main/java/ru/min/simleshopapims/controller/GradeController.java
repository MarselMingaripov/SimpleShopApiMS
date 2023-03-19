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
import ru.min.simleshopapims.service.GradeService;

import java.util.List;

@RestController
@RequestMapping(path = "/grade")
@Tag(name = "API для работы с оценками")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "создать оценку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оценка успешно создана"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей оценки")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade){
        return ResponseEntity.ok().body(gradeService.createGrade(grade));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "удалить оценку по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оценка успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Оценка не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Grade> deleteGrade(@PathVariable(required = true) Long id){
        gradeService.deleteGradeById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить оценку по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оценка успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Оценка не найдена"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей оценки")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Grade> updateGrade(@RequestBody Grade grade, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(gradeService.updateGrade(grade, id));
    }

    @GetMapping("/allWithAS")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список оценок. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оценки успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Grade>> getAllGrades(){
        return ResponseEntity.ok().body(gradeService.findAll());
    }
}
