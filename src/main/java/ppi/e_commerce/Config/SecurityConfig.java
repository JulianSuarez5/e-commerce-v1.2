package ppi.e_commerce.Config;

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
import org.springframework.beans.factory.annotation.Autowired;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authz -> authz
                // Assets estáticos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/webjars/**", "/uploads/**").permitAll()
                
                // APIs públicas
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/products/**").permitAll()
                .requestMatchers("/api/sellers/**").permitAll()
                .requestMatchers("/api/categories/**").permitAll()
                .requestMatchers("/api/brands/**").permitAll()
                
                // Health check
                .requestMatchers("/actuator/health", "/error").permitAll()
                
                // APIs protegidas - Cart y Checkout
                .requestMatchers("/api/cart/**", "/api/checkout/**").authenticated()
                
                // APIs protegidas - Seller actions (upload, variants)
                .requestMatchers("/api/products/*/images", "/api/products/*/models3d", "/api/products/*/variants").hasAnyRole("SELLER", "ADMIN")
                
                // APIs protegidas - Admin
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Todas las demás APIs requieren autenticación
                .requestMatchers("/api/**").authenticated()
                
                // Permitir todo lo demás (frontend Next.js maneja sus propias rutas)
                .anyRequest().permitAll()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Token inválido o expirado\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"No tienes permisos para acceder a este recurso\"}");
                })
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // En producción, especificar dominios
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService(userRepository, authService));
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, AuthServiceImpl authService) {
        return username -> {
            // Buscar por username O email
            java.util.Optional<ppi.e_commerce.Model.User> maybeUser = userRepository.findByUsername(username);
            if (maybeUser.isEmpty()) {
                maybeUser = userRepository.findByEmail(username);
            }

            ppi.e_commerce.Model.User appUser = maybeUser
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            // Verificar que el usuario esté activo
            if (!appUser.isActive()) {
                throw new org.springframework.security.authentication.DisabledException("Usuario desactivado");
            }

            // Normalizar el rol
            String rawRole = appUser.getRole();
            if (rawRole == null || rawRole.isBlank()) {
                rawRole = "USER";
            }
            
            rawRole = rawRole.trim().toUpperCase();
            if (rawRole.startsWith("ROLE_")) {
                rawRole = rawRole.substring(5);
            }
            
            String finalRole = rawRole;

            // Determinar qué contraseña usar
            String passwordToUse;
            if (authService.estaUsandoContrasenaTemporal(appUser)) {
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
