package nl.hu.cisq1.lingo.trainer.presentation.controller;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = InvalidRoundException.class)
    public ResponseEntity<Map<String, String>> roundExceptionHandler(InvalidRoundException e) {
        HashMap<String, String> map = new HashMap();
        map.put("error", e.getMessage());

        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Map<String, String>> notFoundExceptionHandler(NotFoundException e) {
        HashMap<String, String> map = new HashMap();
        map.put("error", e.getMessage());

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }
}
