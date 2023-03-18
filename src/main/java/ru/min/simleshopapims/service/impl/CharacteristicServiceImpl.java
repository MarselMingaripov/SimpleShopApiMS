package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Characteristic;
import ru.min.simleshopapims.repository.CharacteristicRepository;
import ru.min.simleshopapims.service.CharacteristicService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacteristicServiceImpl implements CharacteristicService {

    private final CharacteristicRepository characteristicRepository;
    private final ValidationService validationService;

    @Override
    public Characteristic createCharacteristic(Characteristic characteristic) throws MyValidationException {
        if (validationService.validateCharacteristic(characteristic)) {
            if (!characteristicRepository.existsByName(characteristic.getName())) {
                return characteristicRepository.save(characteristic);
            } else {
                throw new DontExistsByNameException("Current name is already taken!");
            }
        } else {
            throw new MyValidationException("Characteristic has invalid fields");
        }
    }

    @Override
    public void deleteCharacteristicById(Long id) {
        if (characteristicRepository.existsById(id)){
            characteristicRepository.delete(characteristicRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Characteristic not found by ID!");
        }
    }

    @Override
    public Characteristic updateCharacteristic(Characteristic characteristic, Long id) {
        if (characteristicRepository.existsById(id)){
            if (validationService.validateCharacteristic(characteristic)){
                Characteristic ch = characteristicRepository.findById(id).get();
                ch.setName(characteristic.getName());
                return characteristicRepository.save(ch);
            } else {
                throw new MyValidationException("Characteristic has invalid fields");
            }
        } else {
            throw new NotFoundByIdException("Characteristic not found by ID");
        }
    }

    @Override
    public List<Characteristic> findAll() {
        return characteristicRepository.findAll();
    }
}
