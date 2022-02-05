package com.turma20211.mural.service;

import com.turma20211.mural.exception.EmailAlreadyExistsException;
import com.turma20211.mural.exception.UserInvalidEmailException;
import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.exception.UsernameAlreadyExistsException;
import com.turma20211.mural.model.ConfirmationToken;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.UserRepository;
import com.turma20211.mural.utils.Mail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    public Optional<User> findById(Long id) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public String save(User user) throws UserInvalidEmailException, UsernameAlreadyExistsException, EmailAlreadyExistsException, MessagingException, IOException {
        if (user.getEmail().contains("@academico.ifs.edu.br")) {
            user.setUsername(user.getUsername().toLowerCase());
            user.setEmail(user.getEmail().toLowerCase());

            boolean existsUsername = userRepository.existsByUsername(user.getUsername());

            if(existsUsername){
                throw new UsernameAlreadyExistsException(user.getUsername());
            }

            boolean existsEmail = userRepository.existsByEmail(user.getEmail());

            if(existsEmail){
                throw new EmailAlreadyExistsException(user.getEmail());
            }

            User userSaved = userRepository.save(user);

            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    userSaved
                    );

            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = "";
            if(System.getenv("SEND_EMAIL").equals("true")){
                link = "http://muralturma.herokuapp.com/api/v1/user/confirm?token=" + token;
                Mail mailer = new Mail();
                mailer.send(user, link);
            }else{
                link = "http://localhost:8080/api/v1/user/confirm?token=" + token;
                System.out.println(link);
            }

            return token;
        }else{
            throw new UserInvalidEmailException();
        }
    }

    public User update(User user) throws UserNotFoundException {
        User byId = verifyIfExists(user.getId());
        User byUsername = verifyIfExists(user.getUsername());

        if(byId.getId() == byUsername.getId()){
            return userRepository.save(user);
        }

        return new User();
    }

    public String confirmToken(String token) throws UserNotFoundException {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token não encontrado"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("usuário já registrado");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);
        User user = confirmationToken.getUser();

        user.setEnabled(true);
        update(user);
        return "Confirmado!";
    }


    private User verifyIfExists(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
    }

    private User verifyIfExists(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
    }
}
