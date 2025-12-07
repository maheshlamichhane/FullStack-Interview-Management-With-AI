package com.itsutra.project.authentication.util;//package com.itsutra.project.util;
//
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
//
//    @Override
//    public Collection<GrantedAuthority> convert(Jwt jwt) {
//
//        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
//
//        if (realmAccess == null || realmAccess.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        List<String> roles = (List<String>) realmAccess.get("roles");
//
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                .collect(Collectors.toList());
//    }
//}
//
