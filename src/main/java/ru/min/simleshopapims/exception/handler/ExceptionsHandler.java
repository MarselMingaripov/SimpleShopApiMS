package ru.min.simleshopapims.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.min.simleshopapims.exception.*;

@RestControllerAdvice
public class ExceptionsHandler {

    //405
    @ExceptionHandler
    public ResponseEntity<String> myValidationExceptionHandler(MyValidationException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    //406
    @ExceptionHandler
    public ResponseEntity<String> accountIsFrozenExceptionHandler(AccountIsFrozenException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    //409
    @ExceptionHandler
    public ResponseEntity<String> dontExistsByNameExceptionHandler(DontExistsByNameException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    //410
    @ExceptionHandler
    public ResponseEntity<String> itsTooLateExceptionHandler(ItsTooLateException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.GONE);
    }

    //404
    @ExceptionHandler
    public ResponseEntity<String> notFoundByIdExceptionHandler(NotFoundByIdException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    //411
    @ExceptionHandler
    public ResponseEntity<String> productIsNotBoughtExceptionHandler(ProductIsNotBoughtException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.LENGTH_REQUIRED);
    }

    //413
    @ExceptionHandler
    public ResponseEntity<String> tooMuchMoneyExceptionHandler(TooMuchMoneyException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    //400
    @ExceptionHandler
    public ResponseEntity<String> orgStatusExceptionHandler(OrgStatusException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //411
    @ExceptionHandler
    public ResponseEntity<String> emptyBasketExceptionHandler(EmptyBasketException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.LENGTH_REQUIRED);
    }
}
