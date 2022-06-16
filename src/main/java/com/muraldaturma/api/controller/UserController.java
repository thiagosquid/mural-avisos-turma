package com.muraldaturma.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muraldaturma.api.dto.RefreshTokenRequestDto;
import com.muraldaturma.api.dto.mapper.UserMapper;
import com.muraldaturma.api.dto.request.PasswordRecoveryDto;
import com.muraldaturma.api.exception.*;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.security.JWTValidateFilter;
import com.muraldaturma.api.service.UserService;
import com.muraldaturma.api.utils.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.muraldaturma.api.security.JWTAutenticationFilter.TOKEN_PASSWORD_MURAL;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserMapper userMapper; //TODO passar mapper para a service e implementar exception handler nessa classe

    private final UserService userService;
    private final PasswordEncoder encoder;

    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<User> user = null;
        try {
            user = userService.findById(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toDTO(user.get()));
    }

    @PostMapping("/set-admin/{id}")
    public void setAdmin(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            User user = userService.findById(id).get();
            user.setRole(Role.ADMIN);
            String token = request.getHeader(JWTValidateFilter.HEADER_ATTRIBUTE).substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC512(TOKEN_PASSWORD_MURAL);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            Long idSuperUser = Long.parseLong(decodedJWT.getClaim("userId").toString());
            User superUser = userService.findById(idSuperUser).get();
            userService.update(user);
            log.info(String.format("O usuário %s alterou a role do usuário %s para ADMIN", superUser.getUsername(), user.getUsername()));
        } catch (UserNotFoundException e) {
            response.setStatus(404);
            response.sendError(1, "Usuário não encontrado");
        }
    }

    @CrossOrigin("*")
    @PostMapping("/signup")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            userService.save(user);
        } catch (UserInvalidEmailException | UsernameAlreadyExistsException | MessagingException |
                 EmailAlreadyExistsException | FirstNameInvalidException | LastNameInvalidException | IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Registrado com sucesso. Verifique o email cadastrado para confirmar a conta!");
    }

    @CrossOrigin("*")
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        String confirmation = null;
        try {
            confirmation = userService.confirmToken(token);
        } catch (UserNotFoundException | TokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body("ReturnToLogin.html");
    }

    @CrossOrigin("*")
    @GetMapping("/recovery")
    public ResponseEntity<Boolean> recovery(@RequestParam(value = "email") String email) {
        User user = userService.verifyIfEmailExists(email);
        try {
            if (user.getId() != null) {
                userService.changePasswordToken(user);
                return ResponseEntity.ok().body(true);
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

    @CrossOrigin("*")
    @PostMapping("/recovery")
    public ResponseEntity<?> recovery(@RequestBody PasswordRecoveryDto passwordRecoveryDto) {
        passwordRecoveryDto.setPassword(encoder.encode(passwordRecoveryDto.getPassword()));
        try {
            String res = userService.changePassword(passwordRecoveryDto);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (UserNotFoundException | TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/refreshtoken")
    public void refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = "Bearer ".concat(refreshTokenRequestDto.getRefreshToken());
        try {
            Map<String, String> tokens = userService.refreshToken(authorizationHeader);
            response.setStatus(HttpStatus.OK.value());
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (UserNotFoundException e) {
            log.error("Erro em: {}", e.getMessage());
            response.setHeader("error", e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", e.getMessage());

            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
