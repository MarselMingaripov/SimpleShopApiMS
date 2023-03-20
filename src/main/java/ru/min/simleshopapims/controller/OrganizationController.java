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
import ru.min.simleshopapims.model.Organization;
import ru.min.simleshopapims.model.OrganizationStatus;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.model.dto.OrganizationDto;
import ru.min.simleshopapims.service.OrganizationService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/organization")
@Tag(name = "API для работы с организациями")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "создать организацию с активным статусом. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно создан"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей продукта"),
            @ApiResponse(responseCode = "409", description = "Организация с таким именем уже существует")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Organization> createOrganization(@RequestBody OrganizationDto organization){
        return ResponseEntity.ok().body(organizationService.createOrganization(organization));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "удалить организацию по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организация успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Организация не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Organization> deleteOrganization(@PathVariable(required = true) Long id){
        organizationService.deleteOrganizationById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить организацию по ИД. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организация успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Организация не найдена"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей организации")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Organization> updateOrganization(@RequestBody Organization organization, @PathVariable(required = true) Long id){
        return ResponseEntity.ok().body(organizationService.updateOrganization(organization, id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список всех организаций")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организации успешно получены. Только админ")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Organization>> getAllOrganizations(){
        return ResponseEntity.ok().body(organizationService.findAll());
    }

    @PostMapping("/apply-to-create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Отправить запрос на создание организации от пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос успешно отправлен"),
            @ApiResponse(responseCode = "404", description = "Организация с таким именем уже есть"),
            @ApiResponse(responseCode = "405", description = "Ошибка валидации полей организации"),
            @ApiResponse(responseCode = "409", description = "Создать организацию можно только на свое имя")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Organization> applyToCreate(@RequestBody Organization organization){
        return ResponseEntity.ok().body(organizationService.applyToCreateOrg(organization));
    }

    @PostMapping("/make-active/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Установить организации статус активной. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно установлен"),
            @ApiResponse(responseCode = "404", description = "Организация с таким именем не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Organization> makeActive(@PathVariable(required = true) String name){
        return ResponseEntity.ok().body(organizationService.makeActive(name));
    }

    @PostMapping("/make-frozen/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Установить организации статус замороженной. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно установлен"),
            @ApiResponse(responseCode = "404", description = "Организация с таким именем не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Organization> makeFrozen(@PathVariable(required = true) String name){
        return ResponseEntity.ok().body(organizationService.makeFrozen(name));
    }

    @PostMapping("/make-banned/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Установить организации статус забаненной. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно установлен"),
            @ApiResponse(responseCode = "404", description = "Организация с таким именем не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Organization> makeBanned(@PathVariable(required = true) String name){
        return ResponseEntity.ok().body(organizationService.makeBanned(name));
    }

    @GetMapping("/all-by-status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список всех организаций по статусу. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организации успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Organization>> getAllOrganizationsByStatus(@PathVariable(required = true)OrganizationStatus status){
        return ResponseEntity.ok().body(organizationService.findAllByOrganizationStatus(status));
    }

    @GetMapping("/all-by-user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить список всех организаций по пользователю. Только админ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организации успешно получены"),
            @ApiResponse(responseCode = "409", description = "Пользователь не найден")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Organization>> getAllOrganizationsByUser(@PathVariable(required = true)String username){
        return ResponseEntity.ok().body(organizationService.findByOwner(username));
    }

    @GetMapping("/all-own")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить список всех своих организаций")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организации успешно получены")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Organization>> getAllOwnOrganizations(){
        return ResponseEntity.ok().body(organizationService.findOwnOrganizations());
    }

    @GetMapping("/products-by-organization-name/{name}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить список всех продуктов организации по имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Организации успешно получены"),
            @ApiResponse(responseCode = "409", description = "Организации не найдена")})
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Set<Product>> getAllProductsByName(@PathVariable String name){
        return ResponseEntity.ok().body(organizationService.getProductsByOrgName(name));
    }
}
