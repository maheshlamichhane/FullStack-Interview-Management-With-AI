package com.core.project.job.exception;

import org.springframework.graphql.execution.ErrorType;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ApplicationErrors {


    public static <T> Mono<T> departmentAlreadyExistsWithName(String departmentName){
        return Mono.error(new ApplicationException(
                ErrorType.BAD_REQUEST, "Department Already Exists With This Name", Map.of(
                "department name", departmentName
        )));
    }

    public static <T> Mono<T> departmentAlreadyExistWithCode(String code){
        return Mono.error(new ApplicationException(
                ErrorType.BAD_REQUEST, "Department Already Exists With This Code", Map.of(
                "department code", code
        )));
    }

    public static <T> Mono<T> departmentNotFoundById(Long id){
        return Mono.error(new ApplicationException(
                ErrorType.BAD_REQUEST, "Department not found with this id", Map.of(
                "Department id", id
        )));
    }



}
