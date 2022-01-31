package com.turma20211.mural.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turma20211.mural.data.UserDetailData;
import com.turma20211.mural.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter {

    public static final int TOKEN_EXPIRATION = 600_000;

    public static String TOKEN_PASSWORD_MURAL = System.getenv("TOKEN_PASSWORD_MURAL");

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JWTAutenticarFilter(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, UsernameNotFoundException {
        try {

            User user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>()
            );
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar usuario", e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailData userData = (UserDetailData) authResult.getPrincipal();

        String token = JWT.create()
                .withClaim("userId", userData.getId())
                .withSubject(userData.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL));

        response.addHeader("userId", userData.getId().toString());
        response.getWriter().write(token);
        response.getWriter().flush();
    }

}
