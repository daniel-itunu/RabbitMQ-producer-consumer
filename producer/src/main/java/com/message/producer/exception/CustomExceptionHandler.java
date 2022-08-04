package com.message.producer.exception;

import com.message.producer.apiresponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(new ApiResponse("Failed","0",
                "Invalid field provided", errors, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ApiResponse> handleConnectionException(ConnectException exception) {
        return new ResponseEntity<>(new ApiResponse("Failed","0",
                exception.getMessage()+": Check if broker is up and running,"+
                        " as well as producer's host and port it's listening to",
                null, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
