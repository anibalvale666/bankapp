package com.bankking.exception;

import com.bankking.utils.constant.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.bankking.utils.constant.Constants.CODE_ERROR_CLIENT_OR_ACCOUNT_NOT_FOUND;
import static com.bankking.utils.constant.Constants.MESSAGE_ERROR_CLIENT_OR_ACCOUNT_NOT_FOUND;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(ErrorResponse ex) {

        if (ex.getMessage() != null && ex.getMessage().equals(MESSAGE_ERROR_CLIENT_OR_ACCOUNT_NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }
        if (ex.getMessage() != null && ex.getMessage().equals(Constants.MESSAGE_ERROR_UNAVAILABLE_BALANCE)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }
        if (ex.getMessage() != null && ex.getMessage().equals(Constants.MESSAGE_ERROR_DAILY_QUOTA_EXCEEDED)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }
        if (ex.getMessage() != null && ex.getMessage().equals(Constants.MESSAGE_ERROR_FECHAS)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }

        else {
            // En otros casos, devuelve un mensaje de error genérico y el código de estado 500 (Internal Server Error)
            ErrorResponse error = new ErrorResponse(Constants.CODE_ERROR_SERVIDOR, Constants.ERROR_SERVIDOR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}