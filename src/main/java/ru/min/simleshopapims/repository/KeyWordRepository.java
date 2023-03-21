package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.KeyWord;

import java.util.Optional;

public interface KeyWordRepository extends JpaRepository<KeyWord, Long> {

    Boolean existsByName(String name);

    Optional<KeyWord> findByName(String name);
}
