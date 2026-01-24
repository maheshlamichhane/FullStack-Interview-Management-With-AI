//package com.core.project.config;
//
//import com.core.project.job.dto.DepartmentRequestDTO;
//import graphql.schema.TypeResolver;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.graphql.execution.ClassNameTypeResolver;
//import org.springframework.graphql.execution.RuntimeWiringConfigurer;
//
//@Configuration
//public class TypeResolverConfig {
//
//    @Bean
//    public TypeResolver typeResolver(){
//        ClassNameTypeResolver resolver = new ClassNameTypeResolver();
//        resolver.addMapping(DepartmentRequestDTO.class, "Department");
//        return resolver;
//    }
//
//    @Bean
//    public RuntimeWiringConfigurer configurer(TypeResolver resolver){
//        return c -> c.type("DepartmentResponse", b -> b.typeResolver(resolver));
//    }
//
//}
