package com.bankking.exception;

import com.bankking.utils.constant.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {

        if (ex.getMessage() != null && ex.getMessage().equals(Constants.MESSAGE_ERROR_CLIENT_OR_ACCOUNT_NOT_FOUND)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Cliente o cuenta inexistente");
        }
        if (ex.getMessage() != null && ex.getMessage().equals(Constants.MESSAGE_ERROR_UNAVAILABLE_BALANCE)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Saldo no disponible");
        }
        if (ex.getMessage() != null && ex.getMessage().equals(Constants.MESSAGE_ERROR_DAILY_QUOTA_EXCEEDED)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Cupo diario excedido");
        }
        else {
            // En otros casos, devuelve un mensaje de error genérico y el código de estado 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }

    }
}