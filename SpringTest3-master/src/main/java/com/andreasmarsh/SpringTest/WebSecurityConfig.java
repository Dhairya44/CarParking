package com.andreasmarsh.SpringTest;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //.antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/users").authenticated()
                .antMatchers("/admin-portal").hasAuthority("ADMIN")
                .antMatchers("/manage-movies").hasAuthority("ADMIN")
                .antMatchers("/movie-form").hasAuthority("ADMIN")
                .antMatchers("/add-movie").hasAuthority("ADMIN")
                .antMatchers("/delete-movie").hasAuthority("ADMIN")
                .antMatchers("/manage-promotions").hasAuthority("ADMIN")
                .antMatchers("/add-promotion").hasAuthority("ADMIN")
                .antMatchers("/delete-promotion").hasAuthority("ADMIN")
                .antMatchers("/edit-movie").hasAuthority("ADMIN")
                .antMatchers("/edit-promotion").hasAuthority("ADMIN")
                .antMatchers("/send-promotion").hasAuthority("ADMIN")

                .anyRequest().permitAll()
                .and()
                .formLogin()
                    .permitAll()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login_error")
                .and()
                .logout().logoutUrl("/doLogout").logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error");
    }


}