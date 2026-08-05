package com.ram.Learner_Management_System_live.exception;

import com.ram.Learner_Management_System_live.entity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    // Standard Response
//    @ExceptionHandler(LearnerNotFoundException.class) // useful for dispatcher servelet, it will handle exception
//    public ResponseEntity handleLearnerNotFoundException(LearnerNotFoundException e){
//        return ResponseEntity.status(404).body(e.getMessage());
//        // body(e.getStackTrace())....to see all details where it happens, to where it propogate
//    }

    // Custom Response
    @ExceptionHandler(LearnerNotFoundException.class)
    public ErrorResponse handleLearnerNotFoundException(LearnerNotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), new Date().toInstant().toEpochMilli(), ex.getStackTrace().toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach(error -> {
//            String errorMessage = error.getDefaultMessage();
//            String errorField = ((FieldError)error).getField();
//            errors.put(errorField, errorMessage);
//        });

        for(ObjectError error : exception.getBindingResult().getAllErrors()){
            String errorMessage = error.getDefaultMessage();
            String errorField = ((FieldError)error).getField();
            errors.put(errorField, errorMessage);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(CohortNotFoundException.class)
    public ErrorResponse handleCohortNotFoundException(LearnerNotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), new Date().toInstant().toEpochMilli(), ex.getStackTrace().toString());
    }
}
