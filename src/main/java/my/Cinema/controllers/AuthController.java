package my.Cinema.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.Cinema.dtos.LoginRequestDto;
import my.Cinema.dtos.LoginResponseDto;
import my.Cinema.dtos.UserRegisterRequestDto;
import my.Cinema.dtos.UserResponseDto;
import my.Cinema.models.UserModel;
import my.Cinema.services.AuthorizationService;
import my.Cinema.services.TokenService;
import my.Cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor //injeta via lombook
public class AuthController {

    private final UserService userService;

    private final AuthorizationService authorizationService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto>register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        var response= userService.saveUser(userRegisterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto>login(@Valid @RequestBody LoginRequestDto request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        var auth = this.authenticationManager.authenticate(userAndPass);
        var user= (UserModel) auth.getPrincipal();
        //gera token
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDto(token));

    }


}
