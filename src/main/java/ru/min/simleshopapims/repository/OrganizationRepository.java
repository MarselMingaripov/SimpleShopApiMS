package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Organization;
import ru.min.simleshopapims.model.OrganizationStatus;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Boolean existsByName(String name);

    Optional<Organization> findByName(String name);

    List<Organization> findAllByOrganizationStatus(OrganizationStatus organizationStatus);

    List<Organization> findAllByOwner(String owner);
}
