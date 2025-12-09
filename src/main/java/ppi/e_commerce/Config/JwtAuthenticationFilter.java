package ppi.e_commerce.Config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ppi.e_commerce.Utils.JwtUtil;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@org.springframework.lang.NonNull HttpServletRequest request,
                                     @org.springframework.lang.NonNull HttpServletResponse response,
                                     @org.springframework.lang.NonNull FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (ExpiredJwtException e) {
                log.warn("JWT token ha expirado: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("JWT token no soportado: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("JWT token malformado: {}", e.getMessage());
            } catch (SignatureException e) {
                log.warn("Firma JWT inválida: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("Argumento JWT ilegal: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Error al parsear el token JWT", e);
            }
        } else {
            log.debug("La petición no contiene un Bearer token, se omite el filtro JWT.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("Validando token para el usuario: {}", username);
            if (jwtUtil.validateToken(jwtToken, username)) {
                String role = jwtUtil.extractRole(jwtToken);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("Usuario '{}' autenticado correctamente con rol: {}", username, role);
            } else {
                log.warn("Token JWT inválido para el usuario: {}", username);
            }
        }
        chain.doFilter(request, response);
    }
}
