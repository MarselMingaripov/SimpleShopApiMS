package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Organization;
import ru.min.simleshopapims.model.OrganizationStatus;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

    Organization createOrganization(Organization organization) throws MyValidationException;
    void deleteOrganizationById(Long id);
    Organization updateOrganization(Organization organization, Long id);
    List<Organization> findAll();

    void addProfit(Organization organization, double profit);

    Organization applyToCreateOrg(Organization organization);

    Organization makeActive(String name);

    Organization makeFrozen(String name);

    Organization makeBanned(String name);

    List<Organization> findAllByOrganizationStatus(OrganizationStatus organizationStatus);

    List<Organization> findByOwner(String owner);

    Set<Organization> findOwnOrganizations();
}
