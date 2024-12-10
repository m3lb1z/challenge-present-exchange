package dev.emrx.gitfexchange.users.api;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.emrx.gitfexchange.config.security.JwtTokenProvider;
import dev.emrx.gitfexchange.users.dto.LoginRequest;
import dev.emrx.gitfexchange.users.dto.LoginResponse;
import dev.emrx.gitfexchange.users.dto.RegisterUserRequest;
import dev.emrx.gitfexchange.users.model.User;
import dev.emrx.gitfexchange.users.model.UserRole;
import dev.emrx.gitfexchange.users.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public User save(@RequestBody RegisterUserRequest userDTO){
        return this.userService.save(new User(null, userDTO.username(), userDTO.password(), userDTO.email(), Set.of(UserRole.USER)));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginDTO){
        Authentication authDTO = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());

        Authentication authentication = this.authenticationManager.authenticate(authDTO);
        User user = (User) authentication.getPrincipal();

        String token = this.jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                token);
    }
}
