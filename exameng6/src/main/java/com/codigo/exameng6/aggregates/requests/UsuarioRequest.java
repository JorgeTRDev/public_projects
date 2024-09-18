package com.codigo.exameng6.aggregates.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UsuarioRequest class: Estructura de request para los endpoints de este microservicio.
 *
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {

    private String dni;
    private String email;
    private String password;
    private String role;

}
