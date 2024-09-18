package com.codigo.exameng6;

import com.codigo.exameng6.aggregates.requests.LoginRequest;
import com.codigo.exameng6.aggregates.requests.UsuarioRequest;
import com.codigo.exameng6.aggregates.responses.ReniecResponse;
import com.codigo.exameng6.aggregates.responses.UsuarioResponse;
import com.codigo.exameng6.aggregates.token.TokenResponse;
import com.codigo.exameng6.client.ReniecClient;
import com.codigo.exameng6.entity.Role;
import com.codigo.exameng6.entity.UsuarioEntity;
import com.codigo.exameng6.redis.RedisService;
import com.codigo.exameng6.repository.RoleRepository;
import com.codigo.exameng6.repository.UsuarioRepository;
import com.codigo.exameng6.service.JwtService;
import com.codigo.exameng6.service.impl.UserDetailsServiceImpl;
import com.codigo.exameng6.service.impl.UsuarioServiceImpl;
import com.codigo.exameng6.utils.Constants;
import com.codigo.exameng6.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class Exameng6ApplicationTests {

	@Mock
	UsuarioRepository usuarioRepository;
	@Mock
	ReniecClient reniecClient;
	@Mock
	RoleRepository roleRepository;
	AuthenticationManager authenticationManager;
	JwtService jwtService;
	UserDetailsServiceImpl userDetailsService;

	@Mock
	RedisService redisService;

	@InjectMocks
	UsuarioServiceImpl usuarioService;
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		usuarioService = new UsuarioServiceImpl(reniecClient,
				usuarioRepository,
				redisService,
				roleRepository,
				userDetailsService,
				jwtService,
				authenticationManager);
	}

	@Test
	void testCrearUsuario_ok() {
		// respuesta
		UsuarioEntity usuarioEntity = new UsuarioEntity();
		usuarioEntity.setId(1L);
		usuarioEntity.setNombres("Testito");
		usuarioEntity.setApellidoPaterno("Nullberto");
		usuarioEntity.setApellidoMaterno("Pointeros");
		usuarioEntity.setTipoDocumento("DNI");
		usuarioEntity.setNumeroDocumento("77777777");
		usuarioEntity.setDigitoVerificador("1");
		usuarioEntity.setEmail("test@mail.com");
		usuarioEntity.setPassword("abc123");
		usuarioEntity.setEstado(1);
		Role role = new Role();
		role.setIdRole(1);
		role.setNameRole("ADMIN");
		usuarioEntity.setRole(role);
		Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(usuarioEntity);

		UsuarioResponse usuarioResponse =
				new UsuarioResponse(Constants.OK_DNI_CODE, Constants.OK_DNI_MESS, usuarioEntityOptional);
		ReniecResponse reniecResponse = new ReniecResponse(
				"Testito",
				"Nullberto",
				"Pointeros",
				"DNI",
				"77777777",
				"1"
		);
		// input
		UsuarioRequest usuarioRequest = new UsuarioRequest(
				"77777777",
				"test@mail.com",
				"abc123",
				"ADMIN");


		// optional role
		Optional<Role> roleOptional = Optional.of(new Role(1, "ADMIN"));

		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(roleRepository.findByNameRole(Mockito.anyString())).thenReturn(roleOptional);
		Mockito.when(reniecClient.getPersonaReniec(Mockito.anyString(), Mockito.anyString())).thenReturn(reniecResponse);
		Mockito.when(usuarioRepository.save(Mockito.any(UsuarioEntity.class))).thenReturn(usuarioEntity);

		UsuarioResponse respuestaTest = usuarioService.crearUsuario(usuarioRequest);

		assertNotNull(respuestaTest);
		assertEquals(respuestaTest.getCode(), usuarioResponse.getCode());
		assertEquals(respuestaTest.getMessage(), usuarioResponse.getMessage());
		assertEquals(respuestaTest.getData(), usuarioResponse.getData());
	}

	@Test
	void testCrearUsuario_roleNotFound() {
		// respuesta
		UsuarioResponse usuarioResponse =
				new UsuarioResponse(Constants.ERROR_CODE_400, Constants.ERROR_MESS_400, Optional.empty());

		// input
		UsuarioRequest usuarioRequest = new UsuarioRequest(
				"77777777",
				"test@mail.com",
				"abc123",
				"DOC");

		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(roleRepository.findByNameRole(Mockito.anyString())).thenReturn(Optional.empty());

		UsuarioResponse respuestaTest = usuarioService.crearUsuario(usuarioRequest);

		assertNotNull(respuestaTest);
		assertEquals(respuestaTest.getCode(), usuarioResponse.getCode());
		assertEquals(respuestaTest.getMessage(), usuarioResponse.getMessage());
		assertEquals(respuestaTest.getData(), usuarioResponse.getData());
	}

	@Test
	void testCrearUsuario_serverError() {
		/// respuesta
		UsuarioEntity usuarioEntity = new UsuarioEntity();
		usuarioEntity.setId(1L);
		usuarioEntity.setNombres("Testito");
		usuarioEntity.setApellidoPaterno("Nullberto");
		usuarioEntity.setApellidoMaterno("Pointeros");
		usuarioEntity.setTipoDocumento("DNI");
		usuarioEntity.setNumeroDocumento("77777777");
		usuarioEntity.setDigitoVerificador("1");
		usuarioEntity.setEmail("test@mail.com");
		usuarioEntity.setPassword("abc123");
		usuarioEntity.setEstado(1);
		Role role = new Role();
		role.setIdRole(1);
		role.setNameRole("ADMIN");
		usuarioEntity.setRole(role);
		Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(usuarioEntity);

		UsuarioResponse usuarioResponse =
				new UsuarioResponse(Constants.OK_DNI_CODE, Constants.OK_DNI_MESS, usuarioEntityOptional);
		ReniecResponse reniecResponse = new ReniecResponse(
				"Testito",
				"Nullberto",
				"Pointeros",
				"DNI",
				"77777777",
				"1"
		);
		// input
		UsuarioRequest usuarioRequest = new UsuarioRequest(
				"77777777",
				"test@mail.com",
				"abc123",
				"ADMIN");


		// optional role
		Optional<Role> roleOptional = Optional.of(new Role(1, "ADMIN"));

		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(roleRepository.findByNameRole(Mockito.anyString())).thenReturn(roleOptional);
		Mockito.when(reniecClient.getPersonaReniec(Mockito.anyString(), Mockito.anyString())).thenReturn(reniecResponse);
		Mockito.when(usuarioRepository.save(Mockito.any(UsuarioEntity.class))).thenReturn(usuarioEntity);

		UsuarioResponse respuestaTest = usuarioService.crearUsuario(usuarioRequest);

		assertNotNull(respuestaTest);
		assertEquals(respuestaTest.getCode(), usuarioResponse.getCode());
		assertEquals(respuestaTest.getMessage(), usuarioResponse.getMessage());
		assertEquals(respuestaTest.getData(), usuarioResponse.getData());
	}

	@Test
	void testCrearUsuario_exception() {
		// respuesta
		UsuarioEntity usuarioEntity = new UsuarioEntity();
		usuarioEntity.setId(1L);
		usuarioEntity.setNombres("Testito");
		usuarioEntity.setApellidoPaterno("Nullberto");
		usuarioEntity.setApellidoMaterno("Pointeros");
		usuarioEntity.setTipoDocumento("DNI");
		usuarioEntity.setNumeroDocumento("77777777");
		usuarioEntity.setDigitoVerificador("1");
		usuarioEntity.setEmail("test@mail.com");
		usuarioEntity.setPassword("abc123");
		usuarioEntity.setEstado(1);
		Role role = new Role();
		role.setIdRole(1);
		role.setNameRole("ADMIN");
		usuarioEntity.setRole(role);

		UsuarioResponse usuarioErrorResponse =
				new UsuarioResponse(Constants.ERROR_DNI_CODE, Constants.ERROR_DNI_MESS, Optional.empty());
		ReniecResponse reniecResponse = new ReniecResponse(
				"Testito",
				"Nullberto",
				"Pointeros",
				"DNI",
				"77777777",
				"1"
		);
		// input
		UsuarioRequest usuarioRequest = new UsuarioRequest(
				"77777777",
				"test@mail.com",
				"abc123",
				"ADMIN");


		// optional role
		Optional<Role> roleOptional = Optional.of(new Role(1, "ADMIN"));

		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(roleRepository.findByNameRole(Mockito.anyString())).thenReturn(roleOptional);
		Mockito.when(reniecClient.getPersonaReniec(Mockito.anyString(), Mockito.anyString())).thenReturn(reniecResponse);
		Mockito.when(usuarioRepository.save(Mockito.any(UsuarioEntity.class)))
				.thenThrow(new RuntimeException(""));

		UsuarioResponse respuestaTest = usuarioService.crearUsuario(usuarioRequest);

		assertNotNull(respuestaTest);
		assertEquals(respuestaTest.getCode(), usuarioErrorResponse.getCode());
		assertEquals(respuestaTest.getMessage(), usuarioErrorResponse.getMessage());
		assertEquals(respuestaTest.getData(), usuarioErrorResponse.getData());
	}

	@Test
	void testBuscarUsuario_EncontradoEnRedis() {

		// Simulando datos de redis
		String dni = "77777777";
		UsuarioEntity usuarioEntity = new UsuarioEntity();
		usuarioEntity.setNumeroDocumento(dni);
		usuarioEntity.setNombres("Testito");

		String usuarioRedisData = Util.convertirAString(usuarioEntity);

		Mockito.when(redisService.getDataDesdeRedis(Mockito.eq(Constants.REDIS_KEY_API_USERS + dni)))
				.thenReturn(usuarioRedisData);

		// LLamar al metodo
		UsuarioResponse response = usuarioService.buscarUsuario(dni);

		// Asersiones
		assertNotNull(response);
		assertEquals(Constants.OK_DNI_CODE, response.getCode());
		assertTrue(response.getData().isPresent());
	}

	@Test
	void testBuscarUsuario_FoundInBd() {

		// Mock de la BD
		String dni = "77777777";
		UsuarioEntity usuarioEntity = new UsuarioEntity();
		usuarioEntity.setNumeroDocumento(dni);
		usuarioEntity.setNombres("Testito");

		Mockito.when(redisService.getDataDesdeRedis(Mockito.eq(Constants.REDIS_KEY_API_USERS + dni)))
				.thenReturn(null);

		Mockito.when(usuarioRepository.findByNumeroDocumento(Mockito.eq(dni)))
				.thenReturn(Optional.of(usuarioEntity));

		UsuarioResponse response = usuarioService.buscarUsuario(dni);

		// ASersiones
		assertNotNull(response);
		assertEquals(Constants.OK_DNI_CODE, response.getCode());
		assertTrue(response.getData().isPresent());
	}

	@Test
	void testBuscarUsuario_NotFoundInBdOrRedis() {

		// Simular datos de redis
		String dni = "77777777";

		Mockito.when(redisService.getDataDesdeRedis(Mockito.eq(Constants.REDIS_KEY_API_USERS + dni)))
				.thenReturn(null);
		Mockito.when(usuarioRepository.findByNumeroDocumento(Mockito.eq(dni)))
				.thenReturn(Optional.empty());

		// Llamar al metodo
		UsuarioResponse response = usuarioService.buscarUsuario(dni);

		// Asersiones
		assertNotNull(response);
		assertEquals(Constants.ERROR_CODE_LIST_EMPTY, response.getCode());
		assertFalse(response.getData().isPresent());
	}

	@Test
	void testListarUsuarios_Encontrados() {

		// Simulando data
		List<UsuarioEntity> usuariosActivos = Arrays.asList(
				new UsuarioEntity(
						1L,
						"Testito1",
						"Apellido1",
						"Apellido2",
						"DNI",
						"77777771",
						"1",
						"test1@mail.com",
						"abc123",
						Constants.STATUS_ACTIVE,
						null),
				new UsuarioEntity(
						2L,
						"Testito2",
						"Apellido1",
						"Apellido2",
						"DNI",
						"77777772",
						"1",
						"test2@mail.com",
						"abc123",
						Constants.STATUS_ACTIVE,
						null)
		);

		Mockito.when(usuarioRepository.findByEstado(Mockito.eq(Constants.STATUS_ACTIVE)))
				.thenReturn(Optional.of(usuariosActivos));

		// Llamando el metodo
		UsuarioResponse response = usuarioService.listarUsuarios();

		// ASersiones
		assertNotNull(response);
		assertEquals(Constants.OK_DNI_CODE, response.getCode());
		assertTrue(response.getData().isPresent());
	}

	@Test
	void testListarUsuarios_NoEncontrados() {

		// Configurar mock para lista vacia
		Mockito.when(usuarioRepository.findByEstado(Mockito.eq(Constants.STATUS_ACTIVE)))
				.thenReturn(Optional.empty());

		// LLamar el metodo
		UsuarioResponse response = usuarioService.listarUsuarios();

		// Asersiones
		assertNotNull(response);
		assertEquals(Constants.ERROR_CODE_LIST_EMPTY, response.getCode());
		assertFalse(response.getData().isPresent());
	}

	@Test
	void testEliminarUsuario_Eliminado() {

		// Simular data
		UsuarioEntity usuarioRecuperado = new UsuarioEntity(1L, "Testito", "Apellido1", "Apellido2", "DNI",
				"77777777", "1", "test@mail.com", "abc123", Constants.STATUS_ACTIVE, null);

		Mockito.when(usuarioRepository.findByNumeroDocumento(Mockito.eq("77777777")))
				.thenReturn(Optional.of(usuarioRecuperado));

		usuarioRecuperado.setEstado(Constants.STATUS_INACTIVE);
		Mockito.when(usuarioRepository.save(Mockito.any(UsuarioEntity.class)))
				.thenReturn(usuarioRecuperado);

		// Llamar el metodo
		UsuarioResponse response = usuarioService.eliminarUsuario("77777777");

		// Asersiones
		assertNotNull(response);
		assertEquals(Constants.OK_DNI_CODE, response.getCode());
		assertTrue(response.getData().isPresent());
		assertEquals(Constants.STATUS_INACTIVE, ((UsuarioEntity) response.getData().get()).getEstado());
	}

	@Test
	void testEliminarUsuario_UsuarioNoEncontrado() {

		// Data mock
		String dniNoExistente = "77777777";
		Mockito.when(usuarioRepository.findByNumeroDocumento(dniNoExistente)).thenReturn(Optional.empty());

		// Llamar al metodo
		UsuarioResponse respuestaTest = usuarioService.eliminarUsuario(dniNoExistente);

		// Asersiones
		assertNotNull(respuestaTest);
		assertEquals(Constants.ERROR_DNI_CODE, respuestaTest.getCode());
		assertEquals(Constants.ERROR_DNI_MESS, respuestaTest.getMessage());
	}

	@Test
	void testActualizarUsuario_exitoso() {

		// Mockear data
		String dni = "12345678";
		UsuarioRequest usuarioRequest = new UsuarioRequest(
				"12345678",
				"testmail@mail.com",
				"paswordtest",
				"ROLE_USER"
		);

		UsuarioEntity usuarioExistente = new UsuarioEntity();
		usuarioExistente.setEmail("testmailxxxx@mail.com");
		usuarioExistente.setPassword("passwordxxxx");

		UsuarioEntity usuarioActualizado = new UsuarioEntity();
		usuarioActualizado.setEmail(usuarioRequest.getEmail());
		usuarioActualizado.setPassword(new BCryptPasswordEncoder().encode(usuarioRequest.getPassword()));

		when(usuarioRepository.findByNumeroDocumento(dni)).thenReturn(Optional.of(usuarioExistente));
		when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioActualizado);

		// Llamado al metodo
		UsuarioResponse response = usuarioService.actualizarUsuario(dni, usuarioRequest);

		// Asersiones
		assertNotNull(response);
		assertEquals(Constants.OK_DNI_CODE, response.getCode());
		assertEquals(Constants.OK_DNI_MESS, response.getMessage());
	}

	@Test
	void testActualizarUsuario_NotFound() {

		// Mockear data
		String dni = "12345678";
		UsuarioRequest usuarioRequest = new UsuarioRequest(
				"12345678",
				"testmail@mail.com",
				"paswordtest",
				"ROLE_USER"
		);

		when(usuarioRepository.findByNumeroDocumento(dni)).thenReturn(Optional.empty());

		// Llamado al metodo
		UsuarioResponse response = usuarioService.actualizarUsuario(dni, usuarioRequest);

		// Asersiones
		assertNotNull(response);
		assertEquals(Constants.ERROR_DNI_CODE, response.getCode());
		assertEquals(Constants.ERROR_DNI_MESS, response.getMessage());
	}

}
