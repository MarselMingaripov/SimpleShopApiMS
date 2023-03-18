package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.KeyWord;
import ru.min.simleshopapims.repository.KeyWordRepository;
import ru.min.simleshopapims.service.KeyWordService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class KeyWordServiceImpl implements KeyWordService {

    private final ValidationService validationService;
    private final KeyWordRepository keyWordRepository;

    @Override
    public KeyWord createKeyWord(KeyWord keyWord) throws MyValidationException {
        if (validationService.validateKeyWord(keyWord)){
            if (!keyWordRepository.existsByName(keyWord.getName())){
                return keyWordRepository.save(keyWord);
            } else {
                throw new DontExistsByNameException("Current name is already in use");
            }
        } else {
            throw new MyValidationException("KeyWord has invalid fields");
        }
    }

    @Override
    public void deleteKeyWordById(Long id) {
        if (keyWordRepository.existsById(id)){
            keyWordRepository.delete(keyWordRepository.findById(id).get());
        }else {
            throw new DontExistsByNameException("KeyWord dont exists by id!");
        }
    }

    @Override
    public KeyWord updateKeyWord(KeyWord keyWord, Long id) {
        if (keyWordRepository.existsById(id)){
            if (validationService.validateKeyWord(keyWord)){
                KeyWord kw = keyWordRepository.findById(id).get();
                kw.setName(keyWord.getName());
                return keyWordRepository.save(kw);
            } else {
                throw new MyValidationException("KeyWord has invalid fields!");
            }
        } else {
            throw new DontExistsByNameException("KeyWord dont exists by id!");
        }
    }

    @Override
    public List<KeyWord> findAll() {
        return keyWordRepository.findAll();
    }
}
