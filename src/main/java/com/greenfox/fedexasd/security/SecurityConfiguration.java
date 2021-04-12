package com.greenfox.fedexasd.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private AppUserDetailsService userDetailsService;
  private JwtRequestFilter jwtRequestFilter;
  private CustomOAuth2UserService customOAuth2UserService;
  private OAuth2LoginSuccessHandler loginSuccessHandler;

  @Autowired
  public SecurityConfiguration(AppUserDetailsService userDetailsService,
                               JwtRequestFilter jwtRequestFilter,
                               CustomOAuth2UserService customOAuth2UserService,
                               OAuth2LoginSuccessHandler loginSuccessHandler) {
    this.userDetailsService = userDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
    this.customOAuth2UserService = customOAuth2UserService;
    this.loginSuccessHandler = loginSuccessHandler;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/oauth2/**", "/login**").permitAll()
        .antMatchers("/register").permitAll()
        .antMatchers("/login").permitAll()
        .anyRequest().authenticated()
        .and()
        .oauth2Login().loginPage("/login")
        .userInfoEndpoint().userService(customOAuth2UserService)
        .and()
        .successHandler(loginSuccessHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}