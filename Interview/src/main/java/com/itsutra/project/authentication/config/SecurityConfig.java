//package com.itsutra.project.authentication.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) {
//
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/**").permitAll()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                        )
//                );
//
//        return http.build();
//    }
//
//
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:5173"));
//        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/api/**", config);
//        return source;
//    }
//
//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//
//        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
//        converter.setPrincipalClaimName("preferred_username");
//        return converter;
//    }
//
//
//    public class KeycloakJwtGrantedAuthoritiesConverter
//            implements Converter<Jwt, Collection<GrantedAuthority>> {
//        @Override
//        public Collection<GrantedAuthority> convert(Jwt jwt) {
//            System.out.println("Start"+jwt.toString());
//            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
//
//            if (resourceAccess != null && resourceAccess.get("react-frontend") != null) {
//                Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("react-frontend");
//                List<String> clientRoles = (List<String>) clientAccess.get("roles");
//                System.out.println("inside roles"+clientRoles.toString());
//                return clientRoles.stream()
//                        .map((role) -> {
//                            System.out.println("inside role"+role.toString());
//                            return new SimpleGrantedAuthority("ROLE_" + role);
//                        })
//                        .collect(Collectors.toList());
//            }
//            return Collections.emptyList();
//        }
//    }
//}
//
//
