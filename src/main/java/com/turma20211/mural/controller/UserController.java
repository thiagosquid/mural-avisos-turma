package com.turma20211.mural.controller;


import com.turma20211.mural.dto.UserDto;
import com.turma20211.mural.dto.mapper.UserMapper;
import com.turma20211.mural.dto.request.PasswordRecoveryDto;
import com.turma20211.mural.exception.EmailAlreadyExistsException;
import com.turma20211.mural.exception.UserInvalidEmailException;
import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.exception.UsernameAlreadyExistsException;
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
    public ResponseEntity<UserDto> getById(@PathVariable Long id) throws UserNotFoundException {
        Optional<User> user = userService.findById(id);

        if(user.isPresent()){

            return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDto(user.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @CrossOrigin("*")
    @PostMapping("/signup")
    public ResponseEntity<String> saveUser(@RequestBody User user) throws UserInvalidEmailException, UsernameAlreadyExistsException, EmailAlreadyExistsException, MessagingException, IOException {
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);

        return ResponseEntity.ok("");
    }

    @CrossOrigin("*")
    @GetMapping("/confirm")
    public String confirm (@RequestParam String token) throws UserNotFoundException {
        return userService.confirmToken(token);
    }

    @CrossOrigin("*")
    @GetMapping("/recovery")
    public ResponseEntity<Boolean> recovery(@RequestParam(value = "email") String email) throws MessagingException, IOException {
        User user = userService.verifyIfEmailExists(email);
        if(user.getId() != null){
            userService.changePasswordToken(user);
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

    @CrossOrigin("*")
    @PostMapping("/recovery")
    @ResponseStatus(HttpStatus.OK)
    public void recovery(@RequestBody PasswordRecoveryDto passwordRecoveryDto) throws UserNotFoundException {
        passwordRecoveryDto.setPassword(encoder.encode(passwordRecoveryDto.getPassword()));
        userService.changePassword(passwordRecoveryDto);
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
