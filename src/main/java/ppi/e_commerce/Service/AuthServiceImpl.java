package ppi.e_commerce.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ppi.e_commerce.Dto.AuthRequest;
import ppi.e_commerce.Dto.AuthResponse;
import ppi.e_commerce.Dto.RefreshTokenRequest;
import ppi.e_commerce.Dto.RegisterRequest;
import ppi.e_commerce.Exception.AuthenticationException;
import ppi.e_commerce.Exception.UserAlreadyExistsException;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Utils.JwtUtil;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            User user = userService.findByUsername(authRequest.getUsername())
                .or(() -> userService.findByEmail(authRequest.getUsername()))
                .orElseThrow(() -> new AuthenticationException("User not found after successful authentication"));

            return createAuthResponse(user);
        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials for user: {}", authRequest.getUsername());
            throw new AuthenticationException("Invalid credentials");
        }
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userService.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already in use");
        }

        if (userService.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email is already registered");
        }

        User user = userService.registerUser(registerRequest);
        return createAuthResponse(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        try {
            String username = jwtUtil.extractUsername(refreshToken);
            if (jwtUtil.validateToken(refreshToken, username)) {
                User user = userService.findByUsername(username)
                    .orElseThrow(() -> new AuthenticationException("User not found for refresh token"));
                return createAuthResponse(user);
            }
            throw new AuthenticationException("Invalid refresh token");
        } catch (Exception e) {
            throw new AuthenticationException("Invalid refresh token", e);
        }
    }

    @Override
    public AuthResponse validateToken(String token) {
        try {
            String jwtToken = token.substring(7);
            String username = jwtUtil.extractUsername(jwtToken);

            if (jwtUtil.validateToken(jwtToken, username)) {
                User user = userService.findByUsername(username)
                    .orElseThrow(() -> new AuthenticationException("User not found for token validation"));
                return createAuthResponse(user, jwtToken, false);
            }
            throw new AuthenticationException("Invalid token");
        } catch (Exception e) {
            throw new AuthenticationException("Invalid token", e);
        }
    }

    private AuthResponse createAuthResponse(User user) {
        return createAuthResponse(user, null, true);
    }

    private AuthResponse createAuthResponse(User user, String accessToken, boolean generateRefreshToken) {
        String role = user.getRole() != null ? user.getRole().replace("ROLE_", "") : "USER";
        String newAccessToken = (accessToken != null) ? accessToken : jwtUtil.generateToken(user.getUsername(), role);
        String newRefreshToken = generateRefreshToken ? jwtUtil.generateRefreshToken(user.getUsername()) : null;
        return new AuthResponse(newAccessToken, newRefreshToken, user.getUsername(), role, user.getId(), user.getEmail(), user.getName());
    }
}
