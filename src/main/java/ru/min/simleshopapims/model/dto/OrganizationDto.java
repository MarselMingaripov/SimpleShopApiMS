package ru.min.simleshopapims.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrganizationDto {

    private String name;
    private String description;
    private String refToLogo;
    private String owner;

}
