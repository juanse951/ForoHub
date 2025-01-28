package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.usuario.*;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosActualizarPerfil;
import com.aluracursos.Foro.Hub.infra.exceptions.UsuarioNotFoundByIdException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    private Usuario mockSaveUsuario(Usuario usuario, Long id) {
        usuario.setId(id);
        return usuario;
    }

    @Test
    void registrarUsuario_DeberiaDefinirPerfilAdminComoPrimerUsuario() {
        final String NOMBRE_USUARIO = "Admin User";
        final String EMAIL = "admin@example.com";

        DatosRegistroUsuario datosRegistro = new DatosRegistroUsuario(
                NOMBRE_USUARIO,
                EMAIL,
                "Password123!");

        when(usuarioRepository.findUsuarioByCorreoElectronico(datosRegistro.correoElectronico()))
                .thenReturn(Optional.empty());
        when(usuarioRepository.count()).thenReturn(0L);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacion ->
                mockSaveUsuario(invocacion.getArgument(0), 1L));

        DatosRespuestaUsuario respuesta = usuarioService.registrarUsuario(datosRegistro);

        assertNotNull(respuesta);
        assertEquals(NOMBRE_USUARIO, respuesta.nombre());
        assertEquals(EMAIL, respuesta.correoElectronico());
        assertEquals(TipoPerfil.ADMIN, respuesta.perfil());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DebeLanzarExcepcionSiEmailYaExiste() {
        final String EMAIL = "juan.perez@example.com";

        DatosRegistroUsuario datosRegistro = new DatosRegistroUsuario(
                "Juan Perez",
                EMAIL,
                "Password123!");

        when(usuarioRepository.findUsuarioByCorreoElectronico(datosRegistro.correoElectronico()))
                .thenReturn(Optional.of(new Usuario()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario(datosRegistro);
        });

        assertTrue(exception.getMessage().contains("correo electrónico ya está registrado"));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DebeLanzarExcepcionPorEmailInvalido() {
        DatosRegistroUsuario datosRegistro = new DatosRegistroUsuario(
                "Juan Perez",
                "email-invalido",
                "Password123!");

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            usuarioService.registrarUsuario(datosRegistro);
        });

        assertTrue(exception.getMessage().contains("correo electrónico ingresado no es válido"));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DebeAsignarPerfilUsuarioSiNoEsElPrimerUsuario() {
        final String NOMBRE_USUARIO = "Juan Perez";
        final String EMAIL = "juan.perez2@example.com";

        DatosRegistroUsuario datosRegistro = new DatosRegistroUsuario(
                NOMBRE_USUARIO,
                EMAIL,
                "Password123!");

        when(usuarioRepository.findUsuarioByCorreoElectronico(datosRegistro.correoElectronico()))
                .thenReturn(Optional.empty());
        when(usuarioRepository.count()).thenReturn(1L);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacion ->
                mockSaveUsuario(invocacion.getArgument(0), 2L));

        DatosRespuestaUsuario respuesta = usuarioService.registrarUsuario(datosRegistro);

        assertNotNull(respuesta);
        assertEquals(NOMBRE_USUARIO, respuesta.nombre());
        assertEquals(EMAIL, respuesta.correoElectronico());
        assertEquals(TipoPerfil.USER, respuesta.perfil());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DebeEncriptarContrasenaCorrectamente() {
        final String EMAIL = "maria.lopez@example.com";
        final String CONTRASENA = "StrongPass123!";

        DatosRegistroUsuario datosRegistro = new DatosRegistroUsuario(
                "Maria Lopez",
                EMAIL,
                CONTRASENA);

        when(usuarioRepository.findUsuarioByCorreoElectronico(datosRegistro.correoElectronico()))
                .thenReturn(Optional.empty());
        when(usuarioRepository.count()).thenReturn(0L);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacion ->
                mockSaveUsuario(invocacion.getArgument(0), 1L));

        DatosRespuestaUsuario respuesta = usuarioService.registrarUsuario(datosRegistro);

        assertNotNull(respuesta);
        verify(usuarioRepository, times(1)).save(argThat(usuario ->
                passwordEncoder.matches(CONTRASENA, usuario.getContrasena())));
    }

    @Test
    void actualizarUsuario_DebeActualizarUsuarioCorrectamenteCuandoDatosSonValidos() {
        final Long userId = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(userId);
        usuarioExistente.setNombre("Nombre Antiguo");

        DatosActualizarUsuario datosActualizar = new DatosActualizarUsuario("Nombre Nuevo", null, null);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(userId, datosActualizar);

        assertNotNull(usuarioActualizado);
        assertEquals("Nombre Nuevo", usuarioActualizado.getNombre());
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    void actualizarUsuario_DebeLanzarExcepcionSiUsuarioNoExiste() {
        final Long userId = 99L;
        DatosActualizarUsuario datosActualizar = new DatosActualizarUsuario("Nuevo Nombre", null, null);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.actualizarUsuario(userId, datosActualizar);
        });

        assertTrue(exception.getMessage().contains("El usuario con ID " + userId + " no existe."));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_DebeLanzarExcepcionSiEmailYaRegistrado() {
        final Long userId = 1L;
        final String EMAIL_EN_USO = "email.existente@example.com";

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(userId);

        Usuario usuarioConCorreoOcupado = new Usuario();
        usuarioConCorreoOcupado.setId(2L);

        DatosActualizarUsuario datosActualizar = new DatosActualizarUsuario(null, EMAIL_EN_USO, null);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.findUsuarioByCorreoElectronico(EMAIL_EN_USO)).thenReturn(Optional.of(usuarioConCorreoOcupado));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.actualizarUsuario(userId, datosActualizar);
        });

        assertTrue(exception.getMessage().contains("El correo electrónico ingresado ya está registrado."));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_DebeEncriptarNuevaContrasenaCorrectamente() {
        final Long userId = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(userId);

        final String NUEVA_CONTRASENA = "ContrasenaNueva123!";
        DatosActualizarUsuario datosActualizar = new DatosActualizarUsuario(null, null, NUEVA_CONTRASENA);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(userId, datosActualizar);

        assertNotNull(usuarioActualizado);
        verify(usuarioRepository, times(1)).save(argThat(usuario ->
                passwordEncoder.matches(NUEVA_CONTRASENA, usuario.getContrasena())));
    }

    @Test
    void obtenerListadoUsuario_DeberiaDevolverPaginaDeUsuarios() {
        Pageable paginacion = PageRequest.of(0, 10, Sort.by("nombre"));
        Page<Usuario> paginaUsuarios = mock(Page.class);

        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(paginaUsuarios);

        Page<Usuario> resultado = usuarioService.obtenerListadoUsuario(paginacion);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void obtenerListadoUsuario_DeberiaDevolverListaOrdenadaPorNombre() {
        Pageable paginacion = PageRequest.of(0, 10, Sort.by("nombre"));
        List<Usuario> usuarios = List.of(
                new Usuario(1L,"Usuario 1", "usuario1@example.com",
                        "password", TipoPerfil.USER, new ArrayList<>(), new ArrayList<>()),
                new Usuario(2L,"Usuario 2", "usuario2@example.com",
                        "password", TipoPerfil.USER, new ArrayList<>(), new ArrayList<>())
        );
        Page<Usuario> paginaUsuarios = new PageImpl<>(usuarios);

        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(paginaUsuarios);

        Page<Usuario> resultado = usuarioService.obtenerListadoUsuario(paginacion);

        assertNotNull(resultado);
        assertEquals(usuarios.size(), resultado.getContent().size());
        verify(usuarioRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void obtenerListadoUsuario_DeberiaRetornarPaginaVaciaSiNoHayUsuarios() {
        Pageable paginacion = PageRequest.of(0, 10, Sort.by("nombre"));
        Page<Usuario> paginaUsuarios = Page.empty();

        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(paginaUsuarios);

        Page<Usuario> resultado = usuarioService.obtenerListadoUsuario(paginacion);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void obtenerUsuario_DebeDevolverUsuarioSiExisteConId() {
        final Long userId = 1L;

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(userId);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuarioExistente));

        Usuario resultado = usuarioService.obtenerUsuario(userId);

        assertNotNull(resultado);
        assertEquals(usuarioExistente.getId(), resultado.getId());
        verify(usuarioRepository, times(1)).findById(userId);
    }

    @Test
    void obtenerUsuario_DebeLanzarExcepcionSiUsuarioNoExiste() {
        final Long userId = 99L;

        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        UsuarioNotFoundByIdException exception = assertThrows(UsuarioNotFoundByIdException.class, () -> {
            usuarioService.obtenerUsuario(userId);
        });

        assertTrue(exception.getMessage().contains("No se encontró el Usuario con ID " + userId));
        verify(usuarioRepository, times(1)).findById(userId);
    }

    @Test
    void eliminarUsuario_DebeEliminarUsuarioConIdValido() {
        final Long userId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setPerfil(TipoPerfil.USER);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(userId);

        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(userId));
        verify(usuarioRepository, times(1)).deleteById(userId);
    }

    @Test
    void eliminarUsuario_DebeLanzarExcepcionSiUsuarioNoExiste() {
        final Long userId = 99L;

        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        UsuarioNotFoundByIdException exception = assertThrows(UsuarioNotFoundByIdException.class, () -> {
            usuarioService.eliminarUsuario(userId);
        });

        assertTrue(exception.getMessage().contains("No se encontró el Usuario con ID " + userId));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void eliminarUsuario_DebeLanzarExcepcionSiEsPrimerAdmin() {
        final Long userId = 1L;

        Usuario adminUsuario = new Usuario();
        adminUsuario.setId(userId);
        adminUsuario.setPerfil(TipoPerfil.ADMIN);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(adminUsuario));
        when(usuarioRepository.findByPerfil(TipoPerfil.ADMIN)).thenReturn(List.of(adminUsuario));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.eliminarUsuario(userId);
        });

        assertTrue(exception.getMessage().contains("No puedes eliminar al primer administrador registrado."));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }


    @Test
    void actualizarPerfil_DebeActualizarPerfilCuandoEsValido() {
        final Long userId = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(userId);

        DatosActualizarPerfil datosPerfil = new DatosActualizarPerfil(TipoPerfil.MODERATOR);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuarioActualizado = usuarioService.actualizarPerfil(userId, datosPerfil);

        assertNotNull(usuarioActualizado);
        assertEquals(TipoPerfil.MODERATOR, usuarioActualizado.getPerfil());
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    void actualizarPerfil_DebeLanzarExcepcionSiUsuarioNoExiste() {
        final Long userId = 99L;
        DatosActualizarPerfil datosPerfil = new DatosActualizarPerfil(TipoPerfil.USER);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.actualizarPerfil(userId, datosPerfil);
        });

        assertTrue(exception.getMessage().contains("El Usuario con ID " + userId + " no existe."));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}