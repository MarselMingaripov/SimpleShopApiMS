package ru.min.simleshopapims.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Characteristic;
import ru.min.simleshopapims.repository.CharacteristicRepository;
import ru.min.simleshopapims.service.ValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacteristicServiceImplTest {

    private static final String NAME = "nice";
    private Long id = 1L;
    private Characteristic characteristic;
    private List<Characteristic> characteristicList = new ArrayList<>(List.of(new Characteristic(NAME)));

    @Mock
    CharacteristicRepository characteristicRepositoryMock;

    @Mock
    ValidationService validationServiceMock;

    @InjectMocks
    CharacteristicServiceImpl out;

    @BeforeEach
    void init(){
        characteristic = new Characteristic(NAME);
    }

    @Test
    void shouldThrowValidationExceptionWhenFieldsAreIncorrect() {
        Characteristic characteristic1 = new Characteristic("");
        assertThrows(MyValidationException.class, () -> out.createCharacteristic(characteristic1));
    }

    @Test
    void shouldThrowValidationExceptionWhenFieldsAreBlank() {
        Characteristic characteristic1 = new Characteristic("    ");
        assertThrows(MyValidationException.class, () -> out.createCharacteristic(characteristic1));
    }

    @Test
    void shouldReturnNewCharacteristicWhenDontExistsByName() {
        when(validationServiceMock.validateCharacteristic(characteristic)).thenReturn(true);
        when(characteristicRepositoryMock.existsByName(any())).thenReturn(false);
        when(characteristicRepositoryMock.save(any())).thenReturn(characteristic);
        assertEquals(characteristic, out.createCharacteristic(new Characteristic(NAME)));
    }

    @Test
    void shouldReturnCharacteristicWhenExistsByName() {
        when(validationServiceMock.validateCharacteristic(characteristic)).thenReturn(true);
        when(characteristicRepositoryMock.existsByName(any())).thenReturn(true);
        when(characteristicRepositoryMock.findByName(any())).thenReturn(Optional.of(characteristic));
        when(characteristicRepositoryMock.existsById(any())).thenReturn(true);
        when(characteristicRepositoryMock.findById(any())).thenReturn(Optional.of(characteristic));
        when(characteristicRepositoryMock.save(any())).thenReturn(characteristic);
        assertEquals(characteristic, out.createCharacteristic(new Characteristic(NAME)));
    }

    @Test
    void shouldThrowNotFoundByIdExceptionWhenDontExistsById() {
        when(characteristicRepositoryMock.existsById(any())).thenReturn(false);
        assertThrows(NotFoundByIdException.class, () -> out.deleteCharacteristicById(id));
    }

    @Test
    void shouldThrowNotFoundByIdExceptionWhenDontExistsByIdWhenUpdate() {
        when(characteristicRepositoryMock.existsById(any())).thenReturn(false);
        assertThrows(NotFoundByIdException.class, () -> out.updateCharacteristic(characteristic, id));
    }

    @Test
    void shouldThrowMyValidationExceptionWhenFieldIsEmpty() {
        when(characteristicRepositoryMock.existsById(any())).thenReturn(true);
        Characteristic characteristic1 = new Characteristic("");
        assertThrows(MyValidationException.class, () -> out.updateCharacteristic(characteristic1, id));
    }

    @Test
    void shouldReturnCharacteristicWhenUpdate() {
        when(characteristicRepositoryMock.existsById(any())).thenReturn(true);
        when(validationServiceMock.validateCharacteristic(any())).thenReturn(true);
        when(characteristicRepositoryMock.findById(any())).thenReturn(Optional.of(characteristic));
        when(characteristicRepositoryMock.save(any())).thenReturn(characteristic);
        assertEquals(characteristic, out.updateCharacteristic(characteristic, id));
    }

    @Test
    void shouldReturnListOfCharacteristicWhenFindAll(){
        when(characteristicRepositoryMock.findAll()).thenReturn(characteristicList);
        assertEquals(characteristicList, out.findAll());
    }

    @Test
    void shouldReturnCharacteristicWhenFindByName(){
        when(characteristicRepositoryMock.existsByName(any())).thenReturn(true);
        when(characteristicRepositoryMock.findByName(any())).thenReturn(Optional.of(characteristic));
        assertEquals(characteristic, out.findByName(NAME));
    }

    @Test
    void shouldThrowsDontExistsByNameExceptionWhenCharacteristicIsDontExists(){
        when(characteristicRepositoryMock.existsByName(any())).thenReturn(false);
        assertThrows(DontExistsByNameException.class, () -> out.findByName(NAME));

    }
}