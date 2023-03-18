package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Organization;
import ru.min.simleshopapims.repository.OrganizationRepository;
import ru.min.simleshopapims.service.OrganizationService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final ValidationService validationService;
    private final OrganizationRepository organizationRepository;

    @Override
    public Organization createOrganization(Organization organization) throws MyValidationException {
        if (validationService.validateOrganization(organization)){
            if (!organizationRepository.existsByName(organization.getName())){
                return organizationRepository.save(organization);
            } else {
                throw new DontExistsByNameException("Current name is already taken");
            }
        } else {
            throw new MyValidationException("Organization has invalid fields");
        }
    }

    @Override
    public void deleteOrganizationById(Long id) {
        if (organizationRepository.existsById(id)){
            organizationRepository.delete(organizationRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Organization not found!");
        }
    }

    @Override
    public Organization updateOrganization(Organization organization, Long id) {
        if (validationService.validateOrganization(organization)){
            if (organizationRepository.existsById(id)){
                Organization or = organizationRepository.findById(id).get();
                or.setName(organization.getName());
                or.setDescription(organization.getDescription());
                or.setRefToLogo(organization.getRefToLogo());
                or.setProducts(organization.getProducts());
                return organizationRepository.save(or);
            } else {
                throw new NotFoundByIdException("Organization not found!");
            }
        } else {
            throw new MyValidationException("Organization has invalid fields!");
        }
    }

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }
}
