package ppi.e_commerce.Service;

import ppi.e_commerce.Dto.AuthRequest;
import ppi.e_commerce.Dto.AuthResponse;
import ppi.e_commerce.Dto.RefreshTokenRequest;
import ppi.e_commerce.Dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    AuthResponse validateToken(String token);
}
