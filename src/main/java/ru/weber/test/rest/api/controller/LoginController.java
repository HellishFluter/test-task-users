package ru.weber.test.rest.api.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.weber.test.rest.api.dto.LoginDto;
import ru.weber.test.rest.api.dto.Token;
import ru.weber.test.rest.api.dto.UserNewDTO;
import ru.weber.test.rest.api.entity.User;
import ru.weber.test.rest.api.entity.UserCredential;
import ru.weber.test.rest.api.entity.exception.LoginExistException;
import ru.weber.test.rest.api.service.JwtService;
import ru.weber.test.rest.api.service.UserCredentialsService;
import ru.weber.test.rest.api.service.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserCredentialsService userCredentialsService;
    private final UserService userService;
    private final JwtService jwtService;
    private static final String AUTHORIZATION_BEARER = "Bearer";

    @PostMapping("/signin")
    public ResponseEntity<Token> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserCredential user = userCredentialsService.getByLogin(loginDto.getLogin());
        return new ResponseEntity<>(new Token(AUTHORIZATION_BEARER, jwtService.generateToken(user.getUser().getId(), user.getLogin())), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserNewDTO userNewDTO) {
        if (userCredentialsService.existsByLogin(userNewDTO.getLogin())) {
            throw new LoginExistException(userNewDTO.getLogin());
        }
        User user = userService.saveUser(userNewDTO);

        return new ResponseEntity<>(new Token(AUTHORIZATION_BEARER, jwtService.generateToken(user.getId(), userNewDTO.getLogin())), HttpStatus.OK);
    }
}
