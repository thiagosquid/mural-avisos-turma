package com.turma20211.mural.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.turma20211.mural.dto.request.PasswordRecoveryDto;
import com.turma20211.mural.exception.*;
import com.turma20211.mural.model.ConfirmationToken;
import com.turma20211.mural.model.User;
import com.turma20211.mural.repository.UserRepository;
import com.turma20211.mural.utils.Mail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.Collator;
import java.time.LocalDateTime;
import java.util.*;

import static com.turma20211.mural.security.JWTAutenticationFilter.TOKEN_EXPIRATION;
import static com.turma20211.mural.security.JWTAutenticationFilter.TOKEN_PASSWORD_MURAL;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new UserNotFoundException(id);
        }
    }

    public String save(User user) throws UserInvalidEmailException, UsernameAlreadyExistsException,
            EmailAlreadyExistsException, MessagingException, IOException, FirstNameInvalidException, LastNameInvalidException {

        Collator collator = Collator.getInstance(Locale.forLanguageTag("pt_BR"));
        collator.setStrength(Collator.PRIMARY);

        String[] email = user.getEmail().split("[.,@]");
        String emailFirstName = email[0];
        int tam = email[1].length();
        String emailLastName = email[1].substring(0, tam - 3);

        int tamLastName = user.getLastName().split(" ").length;
        //verifica se o primeiro nome é igual ao primeiro nome do email
        boolean sameFirstName = collator.compare(emailFirstName, user.getFirstName()) == 0;
        //verifica se o ultimo nome é igual ao ultimo nome do email
        boolean sameLastName = collator.compare(emailLastName, user.getLastName().split(" ")[tamLastName - 1]) == 0;
        boolean isNumber = false;
        //verifica se os 3 digitos antes da @ são números
        try {
            isNumber = Integer.parseInt(email[1].substring(tam - 3, tam)) >= 0;
        } catch (Exception e) {
            throw new UserInvalidEmailException("Email com 3 dígitos inválidos");
        }

        if (!sameFirstName) {
            throw new FirstNameInvalidException();
        }
        if (!sameLastName) {
            throw new LastNameInvalidException();
        }

        if (user.getEmail().contains("@academico.ifs.edu.br") && isNumber) {
            user.setUsername(user.getUsername());
            user.setEmail(user.getEmail().toLowerCase());
            Optional<ConfirmationToken> ct = Optional.of(new ConfirmationToken());

            Optional<User> userExistent = userRepository.findByEmail(user.getEmail());
            if (userExistent.isPresent()) {
                ct = confirmationTokenService.findByUser(userExistent.get());
            }
            boolean existsUsername = userRepository.existsByUsername(user.getUsername());
            boolean existsEmail = userRepository.existsByEmail(user.getEmail());

            if (ct.isPresent() && existsEmail && LocalDateTime.now().isBefore(ct.get().getExpiresAt())) {
                throw new EmailAlreadyExistsException();
            } else if (existsUsername) {
                throw new UsernameAlreadyExistsException(user.getUsername());
            }

            User userSaved = userRepository.save(user);
            log.info(String.format("Salvando novo usuário como id: %s", userSaved.getId()));
            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    userSaved
            );

            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = "http://muralturma.herokuapp.com/api/v1/user/confirm?token=";
            if (System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")) {
                link = link + token;
                Mail mailer = new Mail();
                mailer.sendConfirmationAccount(user, link);
            } else {
                link = "http://localhost:8080/api/v1/user/confirm?token=" + token;
                System.out.println(link);
            }
            return token;
        } else {
            throw new UserInvalidEmailException();
        }
    }

    public User update(User user) throws UserNotFoundException {
        User byId = verifyIfExists(user.getId());
        User byUsername = verifyIfExists(user.getUsername());

        if (byId.getId() == byUsername.getId()) {
            return userRepository.save(user);
        }

        return new User();
    }

    public String confirmToken(String token) throws UserNotFoundException, TokenException, MessagingException, IOException {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new TokenException("Token não encontrado."));

        if (confirmationToken.getConfirmedAt() != null) {
            return "<h1 style=\"color=red; weight=bold; margin: auto\">Sua Conta já foi verificada</h1>" +
                    "<p style=\"margin: auto;\">Estamos redirecionando para página de login...</p>" +
                    "<script> setTimeout(()=> window.location.replace(\"http://projeto-mural-turma.vercel.app/\"), 3500); </script>";
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        User user = confirmationToken.getUser();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            String newToken = UUID.randomUUID().toString();
            confirmationToken.setToken(newToken);
            confirmationToken.setCreatedAt(LocalDateTime.now());
            confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = "http://muralturma.herokuapp.com/api/v1/user/confirm?token=";
            if (System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")) {
                link = link + newToken;
                Mail mailer = new Mail();
                mailer.sendConfirmationAccount(user, link);
            } else {
                link = "http://localhost:8080/api/v1/user/confirm?token=" + newToken;
                System.out.println(link);
            }
            throw new TokenException("Solicitação expirada. Você receberá novo email de confirmação");
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);

        user.setEnabled(true);
        update(user);
        return "<h1 style=\"color=red; weight=bold; margin: auto\">Conta verificada</h1>" +
                "<p style=\"margin: auto;\">Estamos redirecionando para página de login...</p>" +
                "<script> setTimeout(()=> window.location.replace(\"http://projeto-mural-turma.vercel.app/\"), 3500); </script>";
    }

    public String changePasswordToken(User user) throws MessagingException, IOException {

        Optional<ConfirmationToken> pt = confirmationTokenService.findByUserAndConfirmedAtIsNull(user);

        if (pt.isPresent()) {
            if (pt.get().getUser() != null && pt.get().getConfirmedAt() == null
                    && pt.get().getExpiresAt().isAfter(LocalDateTime.now())) {
                return "Verifique seu email para alterar a senha";
            } else {
                confirmationTokenService.delete(pt.get());
            }
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://projeto-mural-turma.vercel.app/recovery_password?token=";
        if (System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")) {
            link = link + token + "&id=" + user.getId();
            Mail mailer = new Mail();
            mailer.sendRecoveryEmail(user, link);
        } else {
            link = "http://localhost:8080/api/v1/user/confirm?token=" + token + "&id=" + user.getId();
            System.out.println(link);
        }
        return "Um link para alterar a senha foi enviado para o seu email";
    }

    public String changePassword(PasswordRecoveryDto passwordRecoveryDto) throws UserNotFoundException, TokenException {
        User user = userRepository.getById(passwordRecoveryDto.getId());

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(passwordRecoveryDto.getToken()).get();

        if (confirmationToken.getConfirmedAt() != null) {
            throw new TokenException("Esta solicitação de renovação de senha está expirada. Para alterá-la, solicite novamente!");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenException("Esta solicitação de renovação de senha expirada. Solicite uma nova renovação de senha!");
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);
        user.setPassword(passwordRecoveryDto.getPassword());

        this.update(user);

        return "Senha alterada com sucesso";
    }

    public Map<String, String> refreshToken(String authorizationHeader) throws UserNotFoundException {

        Map<String, String> tokens = new HashMap<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refreshToken = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC512(TOKEN_PASSWORD_MURAL);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            Long id = Long.parseLong(decodedJWT.getClaim("userId").toString());
            User user = this.findById(id).get();
            String tokenId = decodedJWT.getId();

            if (!tokenId.equals("1")) {
                throw new RuntimeException("Esse não é um refresh token");
            }
            String accessToken = JWT.create()
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

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
        } else {
            throw new RuntimeException("Refresh token faltando");
        }
        return tokens;
    }

    public User verifyIfEmailExists(String email) {
        return userRepository.findByEmail(email).orElse(new User());
    }

    private User verifyIfExists(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private User verifyIfExists(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private void deleteByEmail(User user) {
        userRepository.delete(user);
    }

    private void sendEmailConfirmation(User user, String token, String link) throws MessagingException, IOException {

        if (System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")) {
            link = link + token;
            Mail mailer = new Mail();
            mailer.sendConfirmationAccount(user, link);
        } else {
            link = "http://localhost:8080/api/v1/user/confirm?token=" + token;
            System.out.println(link);
        }
    }
}
