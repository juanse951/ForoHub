package com.aluracursos.Foro.Hub.infra.security;

import com.aluracursos.Foro.Hub.domain.usuario.TipoPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/login","/usuario/registrar").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/topico/listado", "/respuesta/listado", "/curso/listado","/curso/categoria", "/usuario/listado", "/perfil/listado")
                                .hasAnyAuthority(TipoPerfil.USER.getRole(), TipoPerfil.MODERATOR.getRole(), TipoPerfil.ADMIN.getRole())
                                .requestMatchers(HttpMethod.POST, "/topico/registrar", "/respuesta/registrar/**", "/curso/registrar")
                                .hasAnyAuthority(TipoPerfil.USER.getRole(), TipoPerfil.MODERATOR.getRole(), TipoPerfil.ADMIN.getRole())
                                .requestMatchers(HttpMethod.PUT, "/topico/actualizar/**", "/respuesta/actualizar/**", "/curso/actualizar/**")
                                .hasAnyAuthority(TipoPerfil.MODERATOR.getRole(), TipoPerfil.ADMIN.getRole())
                                .requestMatchers(HttpMethod.PUT, "/perfil/actualizar/**")
                                .hasAnyAuthority(TipoPerfil.ADMIN.getRole())
                                .requestMatchers(HttpMethod.PUT,"/usuario/actualizar/**")
                                .hasAnyAuthority(TipoPerfil.USER.getRole(), TipoPerfil.MODERATOR.getRole(), TipoPerfil.ADMIN.getRole())
                                .requestMatchers(HttpMethod.DELETE, "/topico/eliminar/**", "/respuesta/eliminar/**", "/usuario/eliminar/**", "/curso/eliminar/**")
                                .hasAuthority(TipoPerfil.ADMIN.getRole())
                                .requestMatchers(HttpMethod.GET, "/usuario/buscar/**", "/topico/buscar/**", "/respuesta/buscar/**", "/curso/buscar/**")
                                .hasAnyAuthority(TipoPerfil.USER.getRole(), TipoPerfil.MODERATOR.getRole(), TipoPerfil.ADMIN.getRole())
                                .anyRequest()
                                .authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://forohub.up.railway.app");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}
