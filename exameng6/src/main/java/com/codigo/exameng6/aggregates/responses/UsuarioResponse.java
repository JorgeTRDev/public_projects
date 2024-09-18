package com.codigo.exameng6.aggregates.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

/**
 * UsuarioResponse class: Clase de respuesta para los endpoints de este microservicio.
 *
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {
    private Integer code;
    private String message;
    private Optional data;
}
