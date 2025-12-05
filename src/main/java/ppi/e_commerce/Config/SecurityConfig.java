package ppi.e_commerce.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Service.AuthServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Creando bean de BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Configurando la cadena de filtros de seguridad (SecurityFilterChain).");

        http
            .headers(headers -> {
                log.debug("Aplicando cabeceras de seguridad: CSP, HSTS, Frame-Options.");
                headers
                    .contentSecurityPolicy(csp -> csp
                        .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:; connect-src 'self'; frame-ancestors 'none'; form-action 'self'; object-src 'none';")
                    )
                    .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000)
                    )
                    .frameOptions(frameOptions -> frameOptions.deny());
            })
            .cors(cors -> {
                log.debug("Configurando CORS.");
                cors.configurationSource(corsConfigurationSource());
            })
            .csrf(csrf -> {
                log.debug("Configurando CSRF: ignorando rutas bajo /api/**.");
                csrf.ignoringRequestMatchers("/api/**");
            })
            .sessionManagement(session -> {
                log.debug("Configurando la política de gestión de sesión a STATELESS.");
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authorizeHttpRequests(authz -> {
                log.info("Configurando reglas de autorización de rutas.");
                authz
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/webjars/**", "/uploads/**").permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/products/**").permitAll()
                    .requestMatchers("/api/sellers/**").permitAll()
                    .requestMatchers("/api/categories/**").permitAll()
                    .requestMatchers("/api/brands/**").permitAll()
                    .requestMatchers("/actuator/health", "/error").permitAll()
                    .requestMatchers("/api/cart/**", "/api/checkout/**").authenticated()
                    .requestMatchers("/api/products/*/images", "/api/products/*/models3d", "/api/products/*/variants").hasAnyRole("SELLER", "ADMIN")
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll();
                 log.debug("Reglas de autorización configuradas.");
            })
            .exceptionHandling(exceptions -> {
                log.debug("Configurando manejadores de excepciones de autenticación y acceso denegado.");
                exceptions
                    .authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Acceso no autorizado a {}: {}", request.getRequestURI(), authException.getMessage());
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Token inválido o expirado\"}");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.warn("Acceso denegado a {}: {}", request.getRequestURI(), accessDeniedException.getMessage());
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"No tienes permisos para acceder a este recurso\"}");
                    });
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        log.debug("Añadiendo JwtAuthenticationFilter antes de UsernamePasswordAuthenticationFilter.");

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configurando CORS con orígenes permitidos: {}", Arrays.toString(allowedOrigins));
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        log.info("Obteniendo el AuthenticationManager de la configuración de autenticación.");
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        log.info("Creando DaoAuthenticationProvider.");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService(userRepository, authService));
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, AuthServiceImpl authService) {
        return username -> {
            log.debug("Buscando usuario por username o email: '{}'", username);
            java.util.Optional<ppi.e_commerce.Model.User> maybeUser = userRepository.findByUsername(username);
            if (maybeUser.isEmpty()) {
                log.debug("Usuario no encontrado por username, intentando por email: '{}'", username);
                maybeUser = userRepository.findByEmail(username);
            }

            ppi.e_commerce.Model.User appUser = maybeUser
                    .orElseThrow(() -> {
                        log.warn("Intento de autenticación fallido. Usuario no encontrado: '{}'", username);
                        return new UsernameNotFoundException("Usuario no encontrado: " + username);
                    });

            if (!appUser.isActive()) {
                log.warn("Intento de autenticación para usuario desactivado: '{}'", username);
                throw new org.springframework.security.authentication.DisabledException("Usuario desactivado");
            }
            log.debug("Usuario encontrado y activo: '{}'", appUser.getUsername());

            String rawRole = appUser.getRole();
            if (rawRole == null || rawRole.isBlank()) {
                log.warn("El usuario '{}' no tiene un rol asignado. Asignando rol por defecto 'USER'.", appUser.getUsername());
                rawRole = "USER";
            }
            rawRole = rawRole.trim().toUpperCase();
            if (rawRole.startsWith("ROLE_")) {
                rawRole = rawRole.substring(5);
            }
            String finalRole = rawRole;
            log.debug("Asignando rol '{}' al usuario '{}'", finalRole, appUser.getUsername());

            String passwordToUse;
            if (appUser.getTempPasswordHash() != null && !appUser.getTempPasswordHash().isBlank()) {
                log.debug("Usando contraseña temporal para el usuario '{}'", appUser.getUsername());
                passwordToUse = appUser.getTempPasswordHash();
            } else {
                passwordToUse = appUser.getPassword();
            }

            return User.withUsername(appUser.getUsername())
                .password(passwordToUse)
                .roles(finalRole)
                .disabled(!appUser.isActive())
                .build();
        };
    }
}
