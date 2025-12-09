package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.UserDto;
import ppi.e_commerce.Service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        log.info("GET /api/users/me invoked by user: {}", authentication.getName());
        UserDto user = userService.findUserByUsername(authentication.getName());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateUser(
            @Valid @RequestBody UserDto userDto,
            Authentication authentication) {
        log.info("PUT /api/users/me invoked by user: {}", authentication.getName());
        UserDto updatedUser = userService.updateUser(authentication.getName(), userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        log.info("DELETE /api/users/me invoked by user: {}", authentication.getName());
        userService.deleteUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("GET /api/users invoked");
        List<UserDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
}
