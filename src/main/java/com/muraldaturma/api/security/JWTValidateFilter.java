package com.muraldaturma.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muraldaturma.api.model.User;
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
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.muraldaturma.api.configuration.PropertiesConfiguration.TOKEN_PASSWORD_MURAL;

@Slf4j(topic = "Request")
public class JWTValidateFilter extends BasicAuthenticationFilter {

    public static final String HEADER_ATTRIBUTE = "Authorization";
    public static final String PREFIX_ATTRIBUTE = "Bearer ";

    public static Long REQUEST_USER_ID;

    private final UserService userService;

    public JWTValidateFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        long startTime = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        String session = request.getSession().getId();

        String attribute = request.getHeader(HEADER_ATTRIBUTE);

        if (attribute == null) {
            chain.doFilter(request, response);
            generateLog(request, response, session, startTime);
            return;
        }

        if (!attribute.startsWith(PREFIX_ATTRIBUTE)) {
            chain.doFilter(request, response);
            generateLog(request, response, session, startTime);
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

    protected static void generateLog(HttpServletRequest request, HttpServletResponse response, String session, long startTime){
        long totalTime = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - startTime;

        StringBuilder s = new StringBuilder();
        int count = request.getParameterMap().size();
        if(count > 0){
            s.append("?");
            for(Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()){
                count--;
                s.append(entry.getKey()).append("=").append(Arrays.toString(entry.getValue()));

                if(count > 0){
                    s.append("&");
                }
            }
        }

        log.info("method={} path={} query={} session={} fwd={} status={} service={}ms protocol={}"
                , request.getMethod(), request.getRequestURI(), s, session , request.getRemoteAddr(), response.getStatus(), totalTime, request.getProtocol());
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC512(TOKEN_PASSWORD_MURAL))
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
            User user = userService.findById(userId).get();
            REQUEST_USER_ID = user.getId();
            role = user.getRole();
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
