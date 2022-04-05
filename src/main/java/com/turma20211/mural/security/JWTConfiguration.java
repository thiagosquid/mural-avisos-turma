package com.turma20211.mural.security;

import com.turma20211.mural.service.UserDetailServiceImpl;
import com.turma20211.mural.utils.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class JWTConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_ENDPOINTS = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    private static final String[] POST_ENDPOINTS = {
            "/login",
            "/api/v1/user/signup",
            "/api/v1/user/recovery",
            "/api/v1/file"
    };

    private static final String[] GET_ENDPOINTS = {
            "/api/v1/user/recovery",
            "/api/v1/user/confirm",
            "/api/v1/user/refreshtoken"
    };

    private static final String[] SUPERUSER_ENDPOINTS = {
            "/api/v1/user/set-admin/**",
            "/api/v1/user/all"
    };

    private static final String[] ADMIN_TAG_ENDPOINTS = {
            "/api/v1/tag"
    };

    private final UserDetailServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    public JWTConfiguration(UserDetailServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(SWAGGER_ENDPOINTS).permitAll()
                .antMatchers(HttpMethod.POST,POST_ENDPOINTS).permitAll()
                .antMatchers(HttpMethod.GET, GET_ENDPOINTS).permitAll()
                .antMatchers(SUPERUSER_ENDPOINTS).hasAuthority(Role.SUPERUSER)
                .antMatchers(HttpMethod.POST, ADMIN_TAG_ENDPOINTS).hasAnyAuthority(Role.ADMIN,Role.SUPERUSER)
                .antMatchers(HttpMethod.DELETE, ADMIN_TAG_ENDPOINTS).hasAnyAuthority(Role.ADMIN,Role.SUPERUSER)
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAutenticationFilter(passwordEncoder, authenticationManager()))
                .addFilter(new JWTValidateFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
