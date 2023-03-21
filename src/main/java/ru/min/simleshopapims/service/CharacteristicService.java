package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Characteristic;

import java.util.List;

public interface CharacteristicService {

    Characteristic createCharacteristic(Characteristic characteristic) throws MyValidationException;
    void deleteCharacteristicById(Long id);
    Characteristic updateCharacteristic(Characteristic characteristic, Long id);
    List<Characteristic> findAll();

    Characteristic findByName(String name);
}
