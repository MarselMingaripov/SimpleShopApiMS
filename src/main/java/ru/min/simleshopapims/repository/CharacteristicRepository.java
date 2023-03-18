package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Characteristic;

public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {

    Boolean existsByName(String name);
}
