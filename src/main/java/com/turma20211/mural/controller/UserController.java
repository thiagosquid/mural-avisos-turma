package com.turma20211.mural.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turma20211.mural.data.UserDetailData;
import com.turma20211.mural.dto.mapper.UserMapper;
import com.turma20211.mural.dto.request.PasswordRecoveryDto;
import com.turma20211.mural.exception.*;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.UserRepository;
import com.turma20211.mural.security.JWTAutenticationFilter;
import com.turma20211.mural.security.JWTValidateFilter;
import com.turma20211.mural.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.turma20211.mural.security.JWTAutenticationFilter.TOKEN_EXPIRATION;
import static com.turma20211.mural.security.JWTAutenticationFilter.TOKEN_PASSWORD_MURAL;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    @Autowired
    private UserService userService;

    public UserController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<User> user = null;
        try {
            user = userService.findById(id);
        } catch (UserNotFoundException e) {
            e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDto(user.get()));
    }

    @PostMapping("/setadmin/{id}")
    public void teste(@PathVariable Long id){

        try {
            User user = userService.findById(id).get();
            user.setRole("ADMIN");
            userService.update(user);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(id);
    }

    @CrossOrigin("*")
    @PostMapping("/signup")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            userService.save(user);
        } catch (UserInvalidEmailException | UsernameAlreadyExistsException | MessagingException | EmailAlreadyExistsException | IOException | FirstNameInvalidException | LastNameInvalidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Registrado com sucesso. Verifique o email cadastrado para confirmar a conta!");
    }

    @CrossOrigin("*")
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        String confirmation = null;
        try{
            confirmation = userService.confirmToken(token);
        }catch (UserNotFoundException | TokenException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(confirmation);
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
    public ResponseEntity recovery(@RequestBody PasswordRecoveryDto passwordRecoveryDto){
        passwordRecoveryDto.setPassword(encoder.encode(passwordRecoveryDto.getPassword()));
        try {
            userService.changePassword(passwordRecoveryDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (UserNotFoundException | TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> validatePassword(@RequestParam String username,
                                                    @RequestParam String password) {

        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        User userRegistred = optUser.get();
        boolean valid = encoder.matches(password, userRegistred.getPassword());

        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(valid);
    }

    @GetMapping("/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(JWTValidateFilter.HEADER_ATTRIBUTE);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC512(TOKEN_PASSWORD_MURAL);
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Long id = Long.parseLong(decodedJWT.getClaim("userId").toString());
                User user = userService.findById(id).get();
                String tokenId = decodedJWT.getId();

                if(!tokenId.equals("1")){
                    throw new RuntimeException("Esse não é um refresh token");
                }
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withClaim("userId", user.getId())
                        .withClaim("role", user.getRole())
                        .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                        .sign(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL));

                refreshToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withClaim("userId", user.getId())
                        .withJWTId(String.valueOf(1))
                        .withClaim("role", user.getRole())
                        .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION * 3))
                        .sign(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL));

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refreshToken);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception e){
                log.error("Erro em: {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(403);
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else{
            throw new RuntimeException("Refresh token faltando");
        }
    }

}
