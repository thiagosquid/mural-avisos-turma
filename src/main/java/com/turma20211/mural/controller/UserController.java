package com.turma20211.mural.controller;


import com.turma20211.mural.dto.mapper.UserMapper;
import com.turma20211.mural.dto.request.PasswordRecoveryDto;
import com.turma20211.mural.exception.*;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.UserRepository;
import com.turma20211.mural.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

}
