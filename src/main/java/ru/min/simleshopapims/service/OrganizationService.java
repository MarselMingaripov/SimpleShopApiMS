package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Organization;

import java.util.List;

public interface OrganizationService {

    Organization createOrganization(Organization organization) throws MyValidationException;
    void deleteOrganizationById(Long id);
    Organization updateOrganization(Organization organization, Long id);
    List<Organization> findAll();
}
