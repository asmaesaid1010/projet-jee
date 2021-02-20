package com.sid.authservice.sec.security;

import com.sid.authservice.sec.filters.JwtAuthenticationFilter;
import com.sid.authservice.sec.filters.JwtAuthorizationFilter;
import com.sid.authservice.sec.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //http.formLogin().disable();

        http.headers().frameOptions().disable(); //pour h2-console
        http.authorizeRequests().antMatchers("/h2-console/**", "/login/**", "/refreshToken/**").permitAll();
        //http.authorizeRequests().antMatchers("/h2-console/**", "/login/**", "/refreshToken/**", "/roles/**", "/users/**", "/addRoleToUser/**").permitAll();
        //http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/**", "/roles/**").hasAuthority("USER");
        //http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/**", "/roles/**").hasAuthority("ADMIN");
        //http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/**", "/roles/**", "/addRoleToUser/**").hasAuthority("ADMIN");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
