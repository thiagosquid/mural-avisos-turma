package com.muraldaturma.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muraldaturma.api.data.UserDetailData;
import com.muraldaturma.api.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class JWTAutenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final int TOKEN_EXPIRATION = 10 * 60 * 1000;

    public static String TOKEN_PASSWORD_MURAL = System.getenv("TOKEN_PASSWORD_MURAL");

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JWTAutenticationFilter(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        User user = null;
        try {
            user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority(user.getRole()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                authority
        );

        try {
            return authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            log.warn(e.getMessage());
            Map<String, String> res = new HashMap<>();
            res.put("message", "Usuário ou senha incorretos");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), res);
            } catch (IOException ex) {
                log.warn(ex.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailData userData = (UserDetailData) authResult.getPrincipal();

        String accessToken = JWT.create()
                .withSubject(userData.getUsername())
                .withClaim("userId", userData.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION / 60)) //alterado para teste no front
                .sign(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL));

        String refreshToken = JWT.create()
                .withSubject(userData.getUsername())
                .withClaim("userId", userData.getId())
                .withJWTId("1")
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION / 30)) //alterado para teste no front
                .sign(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
