package com.codigo.exameng6.controller.advice;

import com.codigo.exameng6.aggregates.responses.UsuarioResponse;
import com.codigo.exameng6.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

/**
 * GlobalExceptionHandler class: Manejador de errores general para el microservicio.
 *
 * @version 1.0
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UsuarioResponse> controlandoException(Exception exception){
        UsuarioResponse response = new UsuarioResponse();
        response.setCode(Constants.ERROR_TRX_CODE);
        response.setMessage(Constants.ERROR_TRX_MESS + exception.getMessage());
        response.setData(Optional.empty());
        log.error("EXCEPTION CAPUTRADA EN:. . . controlandoException");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<UsuarioResponse> controlandoAuthException(AuthenticationException authenticationException){
        UsuarioResponse response = new UsuarioResponse();
        response.setCode(Constants.ERROR_CODE_401);
        response.setMessage(Constants.ERROR_MESS_401 + authenticationException.getMessage());
        response.setData(Optional.empty());
        log.error("Autenticación fallida. El token proporcionado no es válido o ha expirado.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}