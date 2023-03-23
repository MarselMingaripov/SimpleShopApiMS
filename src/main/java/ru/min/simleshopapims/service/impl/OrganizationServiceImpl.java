package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Organization;
import ru.min.simleshopapims.model.enums.OrganizationStatus;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.model.dto.OrganizationDto;
import ru.min.simleshopapims.repository.OrganizationRepository;
import ru.min.simleshopapims.security.model.User;
import ru.min.simleshopapims.security.repository.UserRepository;
import ru.min.simleshopapims.service.OrganizationService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.ArrayList;
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
     * @param organizationDto
     * @return
     * @throws MyValidationException
     */
    @Override
    public Organization createOrganization(OrganizationDto organizationDto) throws MyValidationException {
        Organization organization = new Organization(organizationDto.getName(), organizationDto.getDescription(), organizationDto.getRefToLogo(), organizationDto.getOwner());
        if (validationService.validateOrganization(organization)) {
            if (!organizationRepository.existsByName(organization.getName())) {
                organization.setOrganizationStatus(OrganizationStatus.ACTIVE);
                User user = userRepository.findUserByUsername(organization.getOwner());
                List<Organization> organizations = new ArrayList<>();
                organizations.add(organization);
                user.setOrganizations(organizations);
                Organization organization1 = organizationRepository.save(organization);
                userRepository.save(user);
                return organization1;
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
        organizationRepository.save(organization);
    }

    /**
     * для пользователя, создание организации со статусом ожидания
     *
     * @param organizationDto
     * @return
     */
    @Override
    public Organization applyToCreateOrg(OrganizationDto organizationDto) {
        Organization organization = new Organization(organizationDto.getName(), organizationDto.getDescription(), organizationDto.getRefToLogo(), organizationDto.getOwner());
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
            User user = userRepository.findUserByUsername(organization.getOwner());
            if (!user.getOrganizations().contains(organization)){
                List<Organization> organizations = user.getOrganizations();
                organizations.add(organization);
                user.setOrganizations(organizations);
                userRepository.save(user);
            }
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

    @Override
    public Organization findById(Long id){
        if (organizationRepository.existsById(id)){
            return organizationRepository.findById(id).get();
        } else {
            throw new NotFoundByIdException("Organization not found!");
        }
    }

    @Override
    public Set<Product> getProductsByOrgName(String name){
        if (organizationRepository.existsByName(name)){
            return organizationRepository.findByName(name).get().getProducts();
        } else {
            throw new DontExistsByNameException("Organization not found!");
        }
    }

    @Override
    public Organization returnByName(String orgName){
        if (organizationRepository.existsByName(orgName)){
            return organizationRepository.findByName(orgName).get();
        } else {
            throw new DontExistsByNameException("Organization not found!");
        }
    }

}
