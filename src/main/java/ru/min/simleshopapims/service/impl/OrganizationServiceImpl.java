package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Organization;
import ru.min.simleshopapims.model.OrganizationStatus;
import ru.min.simleshopapims.repository.OrganizationRepository;
import ru.min.simleshopapims.security.model.User;
import ru.min.simleshopapims.security.repository.UserRepository;
import ru.min.simleshopapims.service.OrganizationService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final ValidationService validationService;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    /**
     * для админа, создание активной организации
     *
     * @param organization
     * @return
     * @throws MyValidationException
     */
    @Override
    public Organization createOrganization(Organization organization) throws MyValidationException {
        if (validationService.validateOrganization(organization)) {
            if (!organizationRepository.existsByName(organization.getName())) {
                organization.setOrganizationStatus(OrganizationStatus.ACTIVE);
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
        if (organizationRepository.existsById(id)) {
            organizationRepository.delete(organizationRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Organization not found!");
        }
    }

    @Override
    public Organization updateOrganization(Organization organization, Long id) {
        if (validationService.validateOrganization(organization)) {
            if (organizationRepository.existsById(id)) {
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

    @Override
    public void addProfit(Organization organization, double profit) {
        organization.setProfit(organization.getProfit() + profit);
    }

    /**
     * для пользователя, создание организации со статусом ожидания
     *
     * @param organization
     * @return
     */
    @Override
    public Organization applyToCreateOrg(Organization organization) {
        if (validationService.validateOrganization(organization)) {
            if (!organizationRepository.existsByName(organization.getName())) {
                String owner = userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getUsername();
                if (organization.getOwner().equals(owner)) {
                    return organizationRepository.save(organization);
                } else {
                    throw new DontExistsByNameException("You can create organization only with your own username!");
                }
            } else {
                throw new NotFoundByIdException("Current name is already taken");
            }
        } else {
            throw new MyValidationException("Organization has invalid fields");
        }
    }

    @Override
    public Organization makeActive(String name) {
        if (organizationRepository.existsByName(name)) {
            Organization organization = organizationRepository.findByName(name).get();
            organization.setOrganizationStatus(OrganizationStatus.ACTIVE);
            return updateOrganization(organization, organization.getId());
        } else {
            throw new DontExistsByNameException("Organization not found!");
        }
    }

    @Override
    public Organization makeFrozen(String name) {
        if (organizationRepository.existsByName(name)) {
            Organization organization = organizationRepository.findByName(name).get();
            organization.setOrganizationStatus(OrganizationStatus.FROZEN);
            return updateOrganization(organization, organization.getId());
        } else {
            throw new DontExistsByNameException("Organization not found!");
        }
    }

    @Override
    public Organization makeBanned(String name) {
        if (organizationRepository.existsByName(name)) {
            Organization organization = organizationRepository.findByName(name).get();
            organization.setOrganizationStatus(OrganizationStatus.BANNED);
            return updateOrganization(organization, organization.getId());
        } else {
            throw new DontExistsByNameException("Organization not found!");
        }
    }

    @Override
    public List<Organization> findAllByOrganizationStatus(OrganizationStatus organizationStatus) {
        return organizationRepository.findAllByOrganizationStatus(organizationStatus);
    }

    @Override
    public List<Organization> findByOwner(String owner) {
        if (userRepository.existsByUsername(owner)) {
            return organizationRepository.findAllByOwner(owner);
        } else {
            throw new DontExistsByNameException("User not found!");
        }
    }

    @Override
    public List<Organization> findOwnOrganizations(){
        return organizationRepository.findAllByOwner(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
