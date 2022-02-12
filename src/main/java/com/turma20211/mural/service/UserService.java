package com.turma20211.mural.service;

import com.turma20211.mural.dto.request.PasswordRecoveryDto;
import com.turma20211.mural.exception.EmailAlreadyExistsException;
import com.turma20211.mural.exception.UserInvalidEmailException;
import com.turma20211.mural.exception.UserNotFoundException;
import com.turma20211.mural.exception.UsernameAlreadyExistsException;
import com.turma20211.mural.model.ConfirmationToken;
import com.turma20211.mural.model.PasswordToken;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.UserRepository;
import com.turma20211.mural.utils.Mail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PasswordTokenService passwordTokenService;

    public Optional<User> findById(Long id) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public String save(User user) throws UserInvalidEmailException, UsernameAlreadyExistsException, EmailAlreadyExistsException, MessagingException, IOException {
        if (user.getEmail().contains("@academico.ifs.edu.br")) {
            user.setUsername(user.getUsername().toLowerCase());
            user.setEmail(user.getEmail().toLowerCase());
            Optional<ConfirmationToken> ct = Optional.ofNullable(new ConfirmationToken());

            Optional<User> userExistent = userRepository.findByEmail(user.getEmail());
            if(userExistent.isPresent()){
                ct = confirmationTokenService.findByUser(userExistent.get());
            }
            boolean existsUsername = userRepository.existsByUsername(user.getUsername());
            boolean existsEmail = userRepository.existsByEmail(user.getEmail());

            if (existsEmail && LocalDateTime.now().isAfter(ct.get().getExpiresAt()) && ct.isPresent()) {
                deleteByEmail(userExistent.get());
                confirmationTokenService.delete(ct.get());
            } else if (existsEmail && LocalDateTime.now().isBefore(ct.get().getExpiresAt()))
                throw new EmailAlreadyExistsException(user.getEmail());
            else if(existsUsername)
                throw new UsernameAlreadyExistsException(user.getUsername());

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
            if(System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")){
                link = "http://muralturma.herokuapp.com/api/v1/user/confirm?token=" + token;
                Mail mailer = new Mail();
                mailer.sendConfirmationAccount(user, link);
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
                        new IllegalStateException("Token não encontrado"));

        if (confirmationToken.getConfirmedAt() != null) {
            return "<h1 style=\"color=red; weight=bold; margin: auto\">Sua Conta já foi verificada</h1>" +
                    "<p style=\"margin: auto;\">Estamos redirecionando para página de login...</p>" +
                    "<script> setTimeout(()=> window.location.replace(\"http://projeto-mural-turma.vercel.app/\"), 3500); </script>";
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado");
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);
        User user = confirmationToken.getUser();

        user.setEnabled(true);
        update(user);
        return "<h1 style=\"color=red; weight=bold; margin: auto\">Conta verificada</h1>" +
                "<p style=\"margin: auto;\">Estamos redirecionando para página de login...</p>" +
                "<script> setTimeout(()=> window.location.replace(\"http://projeto-mural-turma.vercel.app/\"), 3500); </script>";
    }

    public String changePasswordToken(User user) throws MessagingException, IOException {

        Optional<PasswordToken> pt = passwordTokenService.findByUser(user);

        if(pt.isPresent()){
            if(pt.get().getUser() != null && pt.get().getConfirmedAt() == null
                    && pt.get().getExpiresAt().isAfter(LocalDateTime.now())){
                return "Verifique seu email para alterar a senha";
            }else{
                passwordTokenService.delete(pt.get());
            }
        }

        String token = UUID.randomUUID().toString();

        PasswordToken passwordToken = new PasswordToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        passwordTokenService.savePasswordToken(passwordToken);
        String link = "";
        if(System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")){
            link = "http://projeto-mural-turma.vercel.app/recovery?token=" + token + "&id=" + user.getId();
            Mail mailer = new Mail();
            mailer.sendRecoveryEmail(user, link);
        }else{
            link = "http://projeto-mural-turma.vercel.app/recovery?token=" + token + "&id=" + user.getId();
            System.out.println(link);
        }

        return "";
    }

    public String changePassword(PasswordRecoveryDto passwordRecoveryDto) throws UserNotFoundException {
        User user = userRepository.getById(passwordRecoveryDto.getId());

        PasswordToken passwordToken = passwordTokenService.getToken(passwordRecoveryDto.getToken());

        if (passwordToken.getConfirmedAt() != null) {
            return "Token já utilizado";
        }

        LocalDateTime expiredAt = passwordToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado");
        }

        passwordTokenService.setConfirmedAt(passwordToken);
        user.setPassword(passwordRecoveryDto.getPassword());

        this.update(user);

        return "Senha alterada com sucesso";
    }

    public User verifyIfEmailExists(String email){
        return userRepository.findByEmail(email).orElse(new User());
    }

    private User verifyIfExists(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
    }

    private User verifyIfExists(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
    }

    private void deleteByEmail(User user){
        userRepository.delete(user);
    }
}
