package com.bankking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse extends Throwable {
    private String error;
    private String message;

    @Override
    public Throwable fillInStackTrace() {
        // No hacer nada para evitar llenar el stack trace
        return this;
    }

    @Override
    public void printStackTrace() {
        // No imprimir el stack trace
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        // Devuelve un array vacío para el stack trace
        return new StackTraceElement[0];
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        // No establecer el stack trace
    }
}
