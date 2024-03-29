package com.muraldaturma.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.muraldaturma.api.configuration.PropertiesConfiguration;
import com.muraldaturma.api.dto.request.PasswordRecoveryDto;
import com.muraldaturma.api.exception.*;
import com.muraldaturma.api.model.ConfirmationToken;
import com.muraldaturma.api.model.User;
import com.muraldaturma.api.repository.UserRepository;
import com.muraldaturma.api.utils.Mail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.muraldaturma.api.configuration.PropertiesConfiguration.TOKEN_PASSWORD_MURAL;
import static com.muraldaturma.api.security.JWTAutenticationFilter.TOKEN_EXPIRATION;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private static final String EMAIL_DOMAIN = "@academico.ifs.edu.br";

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
            throw new UserNotFoundException(String.format("Nao foi encontrado usuario com o id: ", id), "user.notFound");
        }
    }

    public void save(User user) throws UserInvalidEmailException, UsernameAlreadyExistsException,
            EmailAlreadyExistsException, MessagingException, IOException, FirstNameInvalidException, LastNameInvalidException {

        if (user.getEmail().contains(EMAIL_DOMAIN)) {
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
                throw new UsernameAlreadyExistsException(String.format("O usuário %s já está na turma", user.getUsername()), "user.alreadyExist");
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
            String link = PropertiesConfiguration.API_BASE_URL.concat("/api/v1/user/confirm?token=").concat(token);
            if (System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")) {
                Mail mailer = new Mail();
                mailer.sendConfirmationAccount(user, link);
            } else {
                System.out.println(link);
            }
        } else {
            throw new UserInvalidEmailException();
        }
    }

    public void update(User user) throws UserNotFoundException {
        User byId = verifyIfExists(user.getId());
        User byUsername = verifyIfExists(user.getUsername());

        if (byId.getId().equals(byUsername.getId())) {
            userRepository.save(user);
            return;
        }
        new User();
    }

    public String confirmToken(String token) throws UserNotFoundException, TokenException, MessagingException, IOException {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new TokenException("Token não encontrado.", "error.token"));

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
            String link = PropertiesConfiguration.API_BASE_URL.concat("/api/v1/user/confirm?token=").concat(newToken);
            if (System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")) {
                Mail mailer = new Mail();
                mailer.sendConfirmationAccount(user, link);
            } else {
                System.out.println(link);
            }
            throw new TokenException("Solicitação expirada. Você receberá novo email de confirmação", "error.token");
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);

        user.setEnabled(true);
        update(user);
        return "<h1 style=\"color=red; weight=bold; margin: auto\">Conta verificada</h1>" +
                "<p style=\"margin: auto;\">Estamos redirecionando para página de login...</p>" +
                "<script> setTimeout(()=> window.location.replace(\"" + PropertiesConfiguration.FRONT_BASE_URL + "\"), 3500); </script>";
    }

    /**
     * @param user
     * @return
     * @throws MessagingException
     * @throws IOException
     */
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
        String link = PropertiesConfiguration.FRONT_BASE_URL.concat("/recovery_password?token=")
                .concat(token)
                .concat("&id=").
                concat(user.getId().toString());

        if (System.getenv("SEND_EMAIL") != null && System.getenv("SEND_EMAIL").equals("true")) {
            Mail mailer = new Mail();
            mailer.sendRecoveryEmail(user, link);
        } else {
            String body = "{\n" +
                    "    \"id\": \"" + user.getId() + "\",\n" +
                    "    \"password\": \"<troque essa senha>\",\n" +
                    "    \"token\" : \"" + token + "\"\n" +
                    "}";
            System.out.println(body);
        }
        return "Um link para alterar a senha foi enviado para o seu email";
    }

    @Transactional
    public String changePassword(PasswordRecoveryDto passwordRecoveryDto) throws UserNotFoundException, TokenException {
        User user = userRepository.getById(passwordRecoveryDto.getId());

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(passwordRecoveryDto.getToken())
                .orElseThrow(()-> new TokenException("Token não encontrado","error.token"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new TokenException("Esta solicitação de renovação já foi utilizada. Faça uma nova requisição!", "error.token");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenException("Esta solicitação de renovação de senha está expirada. Solicite uma nova renovação de senha!", "error.token");
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);
        user.setPassword(passwordRecoveryDto.getPassword());

        this.update(user);

        return "Senha alterada com sucesso";
    }

    public Map<String, String> refreshToken(String authorizationHeader) throws UserNotFoundException, TokenException, TokenExpiredException {

        Map<String, String> tokens = new HashMap<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refreshToken = authorizationHeader.substring("Bearer ".length());
            DecodedJWT decodedJWT = null;
            try {
                decodedJWT = JWT.require(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL))
                        .build()
                        .verify(refreshToken);
            } catch (JWTVerificationException | IllegalArgumentException e) {
                throw new JWTVerificationException(e.getMessage(), new Throwable("refreshToken.expired", null));
            }
            Long id = Long.parseLong(decodedJWT.getClaim("userId").toString());
            User user = this.findById(id).get();
            String tokenId = decodedJWT.getId();

            if (tokenId == null || !tokenId.equals("1")) {
                throw new TokenException("Esse não é um refresh token", "error.token");
            }
            String accessToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("userId", user.getId())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL));

            String newRefreshToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("userId", user.getId())
                    .withJWTId(String.valueOf(1))
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION * 3))
                    .sign(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL));

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", newRefreshToken);
        } else {
            throw new RuntimeException("Refresh token faltando");
        }
        return tokens;
    }

    public User verifyIfEmailExists(String email) {
        return userRepository.findByEmail(email).orElse(new User());
    }

    private User verifyIfExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Nao foi encontrado usuario com o id: ", id), "user.notFound"));
    }

    private User verifyIfExists(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("Nao foi encontrado usuario com o username: ", username), "user.notFound"));
    }

    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }
}
