package com.turma20211.mural.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import java.util.Arrays;
import java.util.Collection;

public class JWTValidateFilter extends BasicAuthenticationFilter {

    public static final String HEADER_ATTRIBUTE = "Authorization";
    public static final String PREFIX_ATTRIBUTE = "Bearer ";

    public JWTValidateFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
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
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

        String user = JWT.require(Algorithm.HMAC512(JWTAutenticationFilter.TOKEN_PASSWORD_MURAL))
                .build()
                .verify(token)
                .getSubject();
        String role = JWT.require(Algorithm.HMAC512(JWTAutenticationFilter.TOKEN_PASSWORD_MURAL))
                .build()
                .verify(token).getClaim("role").asString();
        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));

        if (user == null) {
            return null;
        }
        var tk = new UsernamePasswordAuthenticationToken(user,null, roles);
        return tk;
    }

}
