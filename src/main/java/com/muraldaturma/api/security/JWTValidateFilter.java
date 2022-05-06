package com.muraldaturma.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muraldaturma.api.service.UserService;
import com.muraldaturma.api.utils.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTValidateFilter extends BasicAuthenticationFilter {

    public static final String HEADER_ATTRIBUTE = "Authorization";
    public static final String PREFIX_ATTRIBUTE = "Bearer ";

    private final UserService userService;

    public JWTValidateFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String attribute = request.getHeader(HEADER_ATTRIBUTE);

        if (attribute == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!attribute.startsWith(PREFIX_ATTRIBUTE)) {
            chain.doFilter(request, response);
            return;
        }

        String token = attribute.replace(PREFIX_ATTRIBUTE, "");

        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            authenticationToken = getAuthenticationToken(token);
        } catch (Throwable e) {
            Map<String, String> tokens = new HashMap<>();
            response.setStatus(498);
            tokens.put("message", "Token de acesso expirado");
            tokens.put("code", e.getCause().getMessage());
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC512(JWTAutenticationFilter.TOKEN_PASSWORD_MURAL))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw new JWTVerificationException(e.getMessage(), new Throwable("accessToken.expired",null));
        }

        String username = decodedJWT.getSubject();
        String tokenType = decodedJWT.getId();
        Long userId = decodedJWT.getClaim("userId").asLong();
        Role role = Role.USER;
        try {
            role = userService.findById(userId).get().getRole();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
//        String role = decodedJWT.getClaim("role").asString();

        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role.toString()));

        if (username == null || tokenType != null) {
            return null;
        }
        var tk = new UsernamePasswordAuthenticationToken(username, null, roles);
        return tk;
    }

}
