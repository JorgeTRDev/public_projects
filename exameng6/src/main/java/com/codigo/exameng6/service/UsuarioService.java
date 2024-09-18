package com.codigo.exameng6.service;

import com.codigo.exameng6.aggregates.requests.LoginRequest;
import com.codigo.exameng6.aggregates.requests.UsuarioRequest;
import com.codigo.exameng6.aggregates.responses.UsuarioResponse;
import com.codigo.exameng6.aggregates.token.TokenResponse;

public interface UsuarioService {

    UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest);

    UsuarioResponse listarUsuarios();

    UsuarioResponse eliminarUsuario(String dni);

    UsuarioResponse actualizarUsuario(String dni, UsuarioRequest usuario);

    UsuarioResponse buscarUsuario(String dni);

    TokenResponse loginUsuario(LoginRequest loginRequest);
}
