package ppi.e_commerce.Service;

import ppi.e_commerce.Dto.RegisterRequest;
import ppi.e_commerce.Model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(RegisterRequest registerRequest);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
