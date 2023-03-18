package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Boolean existsByName(String name);
}
