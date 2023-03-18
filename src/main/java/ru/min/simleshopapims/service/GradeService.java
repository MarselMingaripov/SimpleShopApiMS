package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Grade;

import java.util.List;

public interface GradeService {

    Grade createGrade(Grade grade) throws MyValidationException;
    void deleteGradeById(Long id);
    Grade updateGrade(Grade grade, Long id);
    List<Grade> findAll();
}
