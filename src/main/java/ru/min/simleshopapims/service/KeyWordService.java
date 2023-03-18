package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.KeyWord;

import java.util.List;

public interface KeyWordService {

    KeyWord createKeyWord(KeyWord keyWord) throws MyValidationException;
    void deleteKeyWordById(Long id);
    KeyWord updateKeyWord(KeyWord keyWord, Long id);
    List<KeyWord> findAll();
}
