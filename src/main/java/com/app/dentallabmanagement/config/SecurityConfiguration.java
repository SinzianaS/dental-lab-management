package com.app.dentallabmanagement.config;

import com.app.dentallabmanagement.filter.JwtFilter;
import com.app.dentallabmanagement.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;
    @Autowired
    private JwtFilter JwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(customPasswordEncoder.getPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.csrf().disable().cors().disable();

        //using a stateless policy authentication
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http = http.exceptionHandling()
                .authenticationEntryPoint((request, response, exception) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());       //sending 4XX responses code

                }).and();
        http.authorizeHttpRequests()
                .anyRequest().authenticated();


        http.addFilterBefore(JwtFilter, UsernamePasswordAuthenticationFilter.class);  //use our custom created filter before the default filter
    }
}


