package raze.spring.inventory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Value("#{systemEnvironment['ALLOWED_ORIGINS'] ?: 'https://localhost:3000'}")
    private String originAddress;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.addAllowedOrigin(originAddress);
//        config.addAllowedOrigin("https://mega-electric-web-app");
        config.addAllowedOrigin("https://mega-electric-nginx-razar.fandogh.cloud");
//        config.setAllowedOriginPatterns(Arrays.asList("https://*.fandogh.cloud"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");

        config.setAllowedHeaders(Arrays.asList("Authorization", "content-type", "x-auth-token"));
        config.setExposedHeaders(Arrays.asList("x-auth-token", "Authorization"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
