package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
