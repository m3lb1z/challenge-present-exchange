package dev.emrx.gitfexchange.users.api;

import java.util.Set;

import org.hibernate.boot.beanvalidation.IntegrationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.emrx.gitfexchange.config.security.JwtTokenProvider;
import dev.emrx.gitfexchange.users.dto.LoginRequest;
import dev.emrx.gitfexchange.users.dto.LoginResponse;
import dev.emrx.gitfexchange.users.dto.RegisterUserRequest;
import dev.emrx.gitfexchange.users.dto.UserResponse;
import dev.emrx.gitfexchange.users.model.User;
import dev.emrx.gitfexchange.users.model.UserRole;
import dev.emrx.gitfexchange.users.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {
  
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> save(@RequestBody @Valid RegisterUserRequest userRequest, UriComponentsBuilder uriBuilder) {

        if (this.userService.existsByUsername(userRequest.username()) || this.userService.existsByEmail(userRequest.email())) {
            throw new IntegrationException("User already exists");
        } else if (!userRequest.password().equals(userRequest.repassword())) {
            throw new IntegrationException("Passwords no coincide");
        }

        User user = new User(null, userRequest.username(), userRequest.password(), userRequest.email(), Set.of(UserRole.USER));
        user = this.userService.save(user);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
                user.getRoles().stream().map(UserRole::name).toList());

        return ResponseEntity.created(uriBuilder.path("/users/{id}").buildAndExpand(userResponse.id())
                .toUri()).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginDTO){
        Authentication authDTO = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());

        Authentication authentication = this.authenticationManager.authenticate(authDTO);
        User user = (User) authentication.getPrincipal();

        String token = this.jwtTokenProvider.generateToken(authentication);

        LoginResponse loginResponse = new LoginResponse(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(), token);

        return ResponseEntity.ok(loginResponse);
    }
}
