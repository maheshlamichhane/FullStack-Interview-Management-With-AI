package com.core.project.job.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.graphql.ResponseError;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class GenericResponseDTO<T> {

    private final T data;
    private final List<ResponseError> errors;
    private final boolean dataPresent;

    public GenericResponseDTO(T data) {
        this.data = data;
        this.errors = Collections.emptyList();
        this.dataPresent = true;
    }

    public GenericResponseDTO(List<ResponseError> errors) {
        this.data = null;
        this.errors = errors;
        this.dataPresent = false;
    }
}
