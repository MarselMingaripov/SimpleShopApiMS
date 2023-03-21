package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Organization;
import ru.min.simleshopapims.model.enums.OrganizationStatus;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.model.dto.OrganizationDto;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

    Organization createOrganization(OrganizationDto organizationDto) throws MyValidationException;
    void deleteOrganizationById(Long id);
    Organization updateOrganization(Organization organization, Long id);
    List<Organization> findAll();

    void addProfit(Organization organization, double profit);

    Organization applyToCreateOrg(OrganizationDto organizationDto);

    Organization makeActive(String name);

    Organization makeFrozen(String name);

    Organization makeBanned(String name);

    List<Organization> findAllByOrganizationStatus(OrganizationStatus organizationStatus);

    List<Organization> findByOwner(String owner);

    List<Organization> findOwnOrganizations();

    Organization findById(Long id);

    Set<Product> getProductsByOrgName(String name);

    Organization returnByName(String orgName);
}
