package raze.spring.inventory.security.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.WebUtils;
import raze.spring.inventory.security.exception.CustomAccessDeniedHandler;
import raze.spring.inventory.security.exception.CustomAuthenticationEntryPoint;
import raze.spring.inventory.security.jwt.JwtConfig;
import raze.spring.inventory.security.jwt.JwtTokenVerifier;
import raze.spring.inventory.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import raze.spring.inventory.security.service.UserSessionService;


import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
@EnableWebSecurity
public class AppSecurityConfig  {


   @AllArgsConstructor
   @Configuration
   @EnableGlobalMethodSecurity(prePostEnabled = true)
   @Order(1)
   public static class RestSecurityConfig extends WebSecurityConfigurerAdapter {
       private final PasswordEncoder passwordEncoder;
       private final UserDetailsService userDetailsService;
       private final UserSessionService userSessionService;
       private final SecretKey secretKey;
       private final JwtConfig jwtConfig;
       private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
       private final CustomAccessDeniedHandler customAccessDeniedHandler;


       @Override
       protected void configure(HttpSecurity http) throws Exception {
           http.exceptionHandling()
                   .authenticationEntryPoint(customAuthenticationEntryPoint)
                   .accessDeniedHandler(customAccessDeniedHandler);
           http .antMatcher("/v1/**")

//                   .antMatcher("/v2/**")
                   .cors()
                   .and()
                   .csrf()
                   .disable()
                   .sessionManagement()
                   .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                   .and()
                   .addFilter(
                           new JwtUsernameAndPasswordAuthenticationFilter(
                                   this.userSessionService, authenticationManager(), jwtConfig, secretKey))
                   .addFilterAfter(
                           new JwtTokenVerifier(userSessionService, secretKey, jwtConfig),
                           JwtUsernameAndPasswordAuthenticationFilter.class)
                   .authorizeRequests()
//                   .antMatchers("/login/**").permitAll()
//            .antMatchers("/h2-console/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
//            .antMatchers("/secured/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
//            .antMatchers("/v1/resetpassword/**").permitAll()
//            .antMatchers("/v1/download/product/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
//            .antMatchers("/v1/download/small/product/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
//            .antMatchers("/v1/download/small/user/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()

                   .antMatchers( "/v1/forgotpassword").permitAll()
                   .anyRequest()
                   .authenticated()
                   .and()


                   .logout()
//            .addLogoutHandler(new SecurityContextLogoutHandler()).clearAuthentication(false)
                   .logoutUrl("/v1/logout")

                   .logoutSuccessHandler(
                           (req, res, auth) -> {
                               final Cookie authCookie = WebUtils.getCookie(req, "Authorization");

                               String token = authCookie.getValue();
                               log.debug("Removing token: {} ", token);
                               if (token != null) {
                                   this.userSessionService.removeToken(token);
                               }

                               res.setStatus(HttpServletResponse.SC_OK);
                           })
                   .deleteCookies("Authorization");

       }


       @Override
       protected void configure(AuthenticationManagerBuilder auth) throws Exception {
           auth.authenticationProvider(daoAuthenticationProvider());
       }

       @Bean
       public DaoAuthenticationProvider daoAuthenticationProvider() {
           DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
           daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);
           daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
//        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
           return daoAuthenticationProvider;
       }

       @Override
       public void configure(WebSecurity web) throws Exception {
           web.ignoring().antMatchers("/secured/**"
                   , "/error"
                   , "/weblogout"
                   , "/css/**");
       }
   }

   @Configuration
   @Order(2)
   public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

       @Override
       protected void configure(HttpSecurity http) throws Exception {
           http
                   .antMatcher("/usersecurity/**")
                   .sessionManagement()
                   .maximumSessions(1).maxSessionsPreventsLogin(true)
                   .and()
                   .and()
                   .authorizeRequests()
                     .antMatchers( "/usersecurity/confirm" ).permitAll()
                   .antMatchers("/usersecurity/resetpassword").hasAuthority("BASIC")
                   .and()
                   .logout().logoutUrl("/weblogout").logoutRequestMatcher(new AntPathRequestMatcher("/weblogout"))
                   .deleteCookies("JSESSIONID");
       }
   }

}
