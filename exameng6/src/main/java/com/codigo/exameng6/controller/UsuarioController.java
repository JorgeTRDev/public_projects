package com.codigo.exameng6.controller;

import com.codigo.exameng6.aggregates.requests.LoginRequest;
import com.codigo.exameng6.aggregates.requests.UsuarioRequest;
import com.codigo.exameng6.aggregates.responses.UsuarioResponse;
import com.codigo.exameng6.aggregates.token.TokenResponse;
import com.codigo.exameng6.service.UsuarioService;
import com.codigo.exameng6.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Class: UsuarioController <br/>
 *
 * @author <u>Desarrollado por</u>: <br/>
 *         <ul>
 *         <li>Jorge Torres</li>
 *         </ul>
 *         <u>Changes</u>:<br/>
 *         <ul>
 *         <li>09/2024</li>
 *         </ul>
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
        Title: Endpoint encargado de gestionar los registros de usuarios nuevos
        Request: @personaRequest datos para el registro de un usuario
     **/
    @PostMapping("/users/register")
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @RequestBody UsuarioRequest personaRequest){
        UsuarioResponse response = usuarioService.crearUsuario(personaRequest);
        if(response.getCode().equals(Constants.OK_DNI_CODE)){
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
        Title: Endpoint encargado de gestionar los inicios de sesion
        Request: @loginRequest datos para iniciar sesion
     **/
    @PostMapping("/users/login")
    public TokenResponse loginUsuario(@RequestBody LoginRequest loginRequest) {
        return usuarioService.loginUsuario(loginRequest);
    }

    /**
        Title: Endpoint encargado de listar a todos los usuarios activos
     **/
    @GetMapping("/users")
    public ResponseEntity<UsuarioResponse> listar(){
        UsuarioResponse response = usuarioService.listarUsuarios();
        if(response.getCode().equals(Constants.OK_DNI_CODE)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
        Title: Endpoint encargado de eliminar un usuario
        Params: @dni numero de documento
     **/
    @DeleteMapping("/users/{dni}")
    public ResponseEntity<UsuarioResponse> eliminar(@PathVariable("dni") String dni){
        UsuarioResponse response = usuarioService.eliminarUsuario(dni);
        if(response.getCode().equals(Constants.OK_DNI_CODE)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
        Title: Endpoint encargado de actualizar un usuario
        Params: @dni numero de documento
     **/
    @PutMapping("/users/{dni}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable("dni") String dni, @RequestBody UsuarioRequest usuario) {

        UsuarioResponse response = usuarioService.actualizarUsuario(dni, usuario);
        if(response.getCode().equals(Constants.OK_DNI_CODE)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    /**
        Title: Endpoint encargado de buscar un usuario específico
        Params: @dni numero de documento
     **/
    @GetMapping("/users/{dni}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable("dni")String dni){
        UsuarioResponse response = usuarioService.buscarUsuario(dni);
        if(response.getCode().equals(Constants.OK_DNI_CODE)){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
