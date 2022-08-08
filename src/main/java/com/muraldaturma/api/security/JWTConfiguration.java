package com.muraldaturma.api.security;

import com.muraldaturma.api.service.UserDetailServiceImpl;
import com.muraldaturma.api.service.UserService;
import com.muraldaturma.api.utils.Role;
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
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    private static final String[] POST_ENDPOINTS = {
            "/login",
            "/user/signup",
            "/user/recovery",
            "/file", // Included for tests
            "/user/refreshtoken"
    };

    private static final String[] GET_ENDPOINTS = {
            "/user/recovery",
            "/user/confirm",
            "/file" // Included for tests
    };

    private static final String[] SUPERUSER_ENDPOINTS = {
            "/user/set-admin/**",
            "/user/all"
    };

    private static final String[] ADMIN_TAG_ENDPOINTS = {
            "/tag"
    };

    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public JWTConfiguration(UserDetailServiceImpl userDetailService, PasswordEncoder passwordEncoder, UserService userService) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(SWAGGER_ENDPOINTS).permitAll().and().authorizeRequests()
                .antMatchers(HttpMethod.POST, POST_ENDPOINTS).permitAll()
                .antMatchers(HttpMethod.GET, GET_ENDPOINTS).permitAll()
                .antMatchers(SUPERUSER_ENDPOINTS).hasAuthority(Role.SUPERUSER.toString())
                .antMatchers(HttpMethod.POST, ADMIN_TAG_ENDPOINTS).hasAnyAuthority(Role.ADMIN.toString(), Role.SUPERUSER.toString())
                .antMatchers(HttpMethod.DELETE, ADMIN_TAG_ENDPOINTS).hasAnyAuthority(Role.ADMIN.toString(), Role.SUPERUSER.toString())
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAutenticationFilter(passwordEncoder, authenticationManager()))
                .addFilter(new JWTValidateFilter(authenticationManager(), userService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
