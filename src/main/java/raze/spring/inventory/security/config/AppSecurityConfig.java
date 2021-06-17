package raze.spring.inventory.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import raze.spring.inventory.security.exception.CustomAccessDeniedHandler;
import raze.spring.inventory.security.exception.CustomAuthenticationEntryPoint;
import raze.spring.inventory.security.jwt.JwtConfig;
import raze.spring.inventory.security.jwt.JwtTokenVerifier;
import raze.spring.inventory.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import raze.spring.inventory.security.service.UserSessionService;


import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final UserSessionService userSessionService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    public AppSecurityConfig(PasswordEncoder passwordEncoder, @Qualifier("appUserDetailService") UserDetailsService userDetailsService, UserSessionService userSessionService, SecretKey secretKey, JwtConfig jwtConfig, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.userSessionService = userSessionService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
        .cors()
        .and()
        .addFilter(
            new JwtUsernameAndPasswordAuthenticationFilter(
                this.userSessionService, authenticationManager(), jwtConfig, secretKey))
        .addFilterAfter(
            new JwtTokenVerifier(this.userSessionService, secretKey, jwtConfig),
            JwtUsernameAndPasswordAuthenticationFilter.class)
        .authorizeRequests()
//            .antMatchers("/h2-console/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
            .antMatchers("/secured/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
            .antMatchers("/v1/download/product/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
            .antMatchers("/v1/download/small/product/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
            .antMatchers("/v1/download/small/user/**").permitAll().and().headers().frameOptions().disable().and().authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .logout()
        .logoutUrl("/logout")
        .addLogoutHandler(new SecurityContextLogoutHandler())
        //        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessHandler(
            (req, res, auth) -> {
              String token = req.getHeader("Authorization");
              log.debug("Removing token: {}", token);
              if (token != null) {
                  this.userSessionService.removeToken(token.substring(7));
              }
              res.setStatus(HttpServletResponse.SC_OK);
            })
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(customAccessDeniedHandler);
    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "content-type", "x-auth-token"));
//        corsConfiguration.setExposedHeaders(Arrays.asList("x-auth-token", "Authorization"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return source;
//    }


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
        web.ignoring().antMatchers("/secured/**");
        super.configure(web);
    }
}
