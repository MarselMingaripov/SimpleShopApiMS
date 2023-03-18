package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.KeyWord;

public interface KeyWordRepository extends JpaRepository<KeyWord, Long> {

    Boolean existsByName(String name);
}
