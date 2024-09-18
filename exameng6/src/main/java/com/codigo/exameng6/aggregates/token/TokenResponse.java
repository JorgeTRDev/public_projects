package com.codigo.exameng6.aggregates.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TokenResponse class: Estructura del token devuelto al logearse.
 *
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    String accessToken;

}
