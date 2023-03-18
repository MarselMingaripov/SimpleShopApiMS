package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Grade;
import ru.min.simleshopapims.repository.GradeRepository;
import ru.min.simleshopapims.service.GradeService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final ValidationService validationService;
    private final GradeRepository gradeRepository;

    @Override
    public Grade createGrade(Grade grade) throws MyValidationException {
        if (validationService.validateGrade(grade)){
            return gradeRepository.save(grade);
        } else {
            throw new MyValidationException("Grade has invalid fields!");
        }
    }

    @Override
    public void deleteGradeById(Long id) {
        if (gradeRepository.existsById(id)){
            gradeRepository.delete(gradeRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Grade not found by id!");
        }
    }

    @Override
    public Grade updateGrade(Grade grade, Long id) {
        if (gradeRepository.existsById(id)){
            if (validationService.validateGrade(grade)){
                Grade gr = gradeRepository.findById(id).get();
                gr.setGrade(grade.getGrade());
                return gradeRepository.save(gr);
            } else {
                throw new MyValidationException("Grade has invalid fields");
            }
        } else {
            throw new NotFoundByIdException("Grade not found by id");
        }
    }

    @Override
    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }
}
