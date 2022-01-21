package com.turma20211.mural.controller;


import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.UserRepository;
import com.turma20211.mural.utils.Mail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/signup")
    public ResponseEntity<User> saveUser(@RequestBody User user) throws MessagingException, IOException {
        user.setPassword(encoder.encode(user.getPassword()));
        User userSaved = userRepository.save(user);
//        if(userSaved != null){
//            Mail mailer = new Mail();
//            mailer.send(userSaved);
//        }

        return ResponseEntity.ok(userRepository.save(user));
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
