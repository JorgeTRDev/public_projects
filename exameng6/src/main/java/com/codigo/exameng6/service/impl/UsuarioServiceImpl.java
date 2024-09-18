package com.codigo.exameng6.service.impl;

import com.codigo.exameng6.aggregates.requests.LoginRequest;
import com.codigo.exameng6.aggregates.requests.UsuarioRequest;
import com.codigo.exameng6.aggregates.responses.UsuarioResponse;
import com.codigo.exameng6.aggregates.responses.ReniecResponse;
import com.codigo.exameng6.aggregates.token.TokenResponse;
import com.codigo.exameng6.client.ReniecClient;
import com.codigo.exameng6.entity.Role;
import com.codigo.exameng6.entity.UsuarioEntity;
import com.codigo.exameng6.redis.RedisService;
import com.codigo.exameng6.repository.RoleRepository;
import com.codigo.exameng6.repository.UsuarioRepository;
import com.codigo.exameng6.service.JwtService;
import com.codigo.exameng6.service.UsuarioService;
import com.codigo.exameng6.utils.Constants;
import com.codigo.exameng6.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final ReniecClient reniecClient;
    private final UsuarioRepository usuarioRepository;
    private final RedisService redisService;
    private final RoleRepository roleRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${token.api}")
    private String tokenapi;

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        Optional<UsuarioEntity> usuarioOptional = usuarioRepository.findByEmail(request.getEmail());
        Optional<Role> roleOptional = roleRepository.findByNameRole(request.getRole());
        if (usuarioOptional.isPresent() || roleOptional.isEmpty()) {
            return new UsuarioResponse(Constants.ERROR_CODE_400,Constants.ERROR_MESS_400,
                    Optional.empty());
        }
        try {
            UsuarioEntity usuario = getEntity(request, roleOptional);
            if(Objects.nonNull(usuario)){
                return new UsuarioResponse(Constants.OK_DNI_CODE,Constants.OK_DNI_MESS,
                        Optional.of(usuarioRepository.save(usuario)));
            }
        }catch (Exception e){
            return new UsuarioResponse(Constants.ERROR_DNI_CODE
                    ,Constants.ERROR_DNI_MESS + e.getMessage(), Optional.empty());
        }

        return null;
    }
    @Override
    public UsuarioResponse listarUsuarios() {
        Optional<List<UsuarioEntity>> usuarioEntityList = usuarioRepository.findByEstado(Constants.STATUS_ACTIVE);

        if(usuarioEntityList.isPresent()){
            return new UsuarioResponse(Constants.OK_DNI_CODE,Constants.OK_DNI_MESS,
                    usuarioEntityList);
        }else {
            return new UsuarioResponse(Constants.ERROR_CODE_LIST_EMPTY
                    ,Constants.ERROR_MESS_LIST_EMPTY, Optional.empty());
        }
    }

    @Override
    public UsuarioResponse eliminarUsuario(String dni) {
        UsuarioResponse response = new UsuarioResponse();
        Optional<UsuarioEntity> usuarioRecuperado = usuarioRepository.findByNumeroDocumento(dni);
        if(usuarioRecuperado.isPresent()){
            usuarioRecuperado.get().setEstado(Constants.STATUS_INACTIVE);
            response.setCode(Constants.OK_DNI_CODE);
            response.setMessage(Constants.OK_DNI_MESS);
            response.setData(Optional.of(usuarioRepository.save(usuarioRecuperado.get())));
            return response;
        }else {
            response.setCode(Constants.ERROR_DNI_CODE);
            response.setMessage(Constants.ERROR_DNI_MESS);
            response.setData(Optional.of(Optional.empty()));
            return response;
        }
    }

    @Override
    public UsuarioResponse actualizarUsuario(String dni, UsuarioRequest usuario) {

        Optional<UsuarioEntity> personaExistente = usuarioRepository.findByNumeroDocumento(dni);

        if(personaExistente.isPresent()) {
            UsuarioEntity datosActualizar = personaExistente.get();
            datosActualizar.setEmail(usuario.getEmail());
            datosActualizar.setPassword(usuario.getPassword());
            datosActualizar.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            return new UsuarioResponse(Constants.OK_DNI_CODE,Constants.OK_DNI_MESS,
                    Optional.of(usuarioRepository.save(datosActualizar)));
        } else {
            return new UsuarioResponse(Constants.ERROR_DNI_CODE, Constants.ERROR_DNI_MESS,
                    Optional.of(new UsuarioEntity()));
        }
    }

    @Override
    public UsuarioResponse buscarUsuario(String dni) {
        try {
            UsuarioEntity usuarioEntity = executionReniecRedis(dni);
            if(usuarioEntity == null) {
                return new UsuarioResponse(Constants.ERROR_CODE_LIST_EMPTY
                        ,Constants.ERROR_MESS_LIST_EMPTY, Optional.empty());
            }
            return new UsuarioResponse(Constants.OK_DNI_CODE,Constants.OK_DNI_MESS, Optional.of(usuarioEntity));
        }catch (Exception e){
            return new UsuarioResponse(Constants.ERROR_DNI_CODE,Constants.ERROR_DNI_MESS,Optional.empty());
        }
    }

    private ReniecResponse executionReniec(String documento){
        String auth = "Bearer "+tokenapi;
        return reniecClient.getPersonaReniec(documento,auth);
    }

    private UsuarioEntity executionReniecRedis(String documento){
        //API:USERS:DNI:46027897
        String redisInfo = redisService.getDataDesdeRedis(Constants.REDIS_KEY_API_USERS+documento);
        if (Objects.nonNull(redisInfo)){
            return Util.convertirDesdeString(redisInfo,UsuarioEntity.class);
        } else {
            Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByNumeroDocumento(documento);

            if(usuarioEntity.isPresent()){
                String dataForRedis = Util.convertirAString(usuarioEntity.get());
                redisService.guardarEnRedis(Constants.REDIS_KEY_API_USERS+documento,dataForRedis,Constants.REDIS_EXP);
                return usuarioEntity.get();
            }else {
                return null;
            }
        }
    }

    private UsuarioEntity getEntity(UsuarioRequest usuarioRequest, Optional<Role> roleOptional){

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        ReniecResponse response = executionReniec(usuarioRequest.getDni());
        if (Objects.nonNull(response)){
            usuarioEntity.setNombres(response.getNombres());
            usuarioEntity.setApellidoPaterno(response.getApellidoPaterno());
            usuarioEntity.setApellidoMaterno(response.getApellidoMaterno());
            usuarioEntity.setNumeroDocumento(response.getNumeroDocumento());
            usuarioEntity.setTipoDocumento(response.getTipoDocumento());
            usuarioEntity.setDigitoVerificador(response.getDigitoVerificador());
            usuarioEntity.setEmail(usuarioRequest.getEmail());
            usuarioEntity.setEstado(Constants.STATUS_ACTIVE);
            usuarioEntity.setPassword(new BCryptPasswordEncoder().encode(usuarioRequest.getPassword()));
            usuarioEntity.setRole(roleOptional.get());
            return usuarioEntity;
        }
        return null;
    }

    @Override
    public TokenResponse loginUsuario(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (auth.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setAccessToken(jwtService.generateToken(userDetails));
            return tokenResponse;
        }
        throw new AuthenticationException("Usuario o contraseña inválidos") {
            private static final long serialVersionUID = 1L;
        };
    }
}
