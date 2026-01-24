package com.core.project.job.service;

import com.core.project.job.dto.DeleteResponseDTO;
import com.core.project.job.dto.DepartmentRequestDTO;
import com.core.project.job.dto.DepartmentResponseDTO;
import com.core.project.job.dto.GenericResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DepartmentClientService {

    private final HttpGraphQlClient client;
    private static final String DEPARTMENT_URL = "http://localhost:8080/graphql";

    public DepartmentClientService() {
        this.client = HttpGraphQlClient.builder()
                .webClient(b -> b.baseUrl(DEPARTMENT_URL))
                .build();
    }


    public Mono<ClientGraphQlResponse> rawQuery(String query) {
        return this.client.document(query)
                .execute();
    }

    public Mono<GenericResponseDTO<DepartmentResponseDTO>> getDepartmentById(Integer id) {
        return this.client
                .documentName("department-by-id")
                .variable("id", id)
                .execute()
                .map(cr -> {
                    var field = cr.field("getDepartmentById");
                    return Objects.nonNull(field.getValue()) ? new GenericResponseDTO<>(field.toEntity(DepartmentResponseDTO.class)) :
                            new GenericResponseDTO<>(field.getErrors());
                });
    }

    public Mono<List<DepartmentResponseDTO>> allDepartments() {
        return this.crud("GetAllDepartment", Collections.emptyMap(), new ParameterizedTypeReference<List<DepartmentResponseDTO>>() {});
    }



    public Mono<DepartmentResponseDTO> createDepartment(DepartmentRequestDTO dto) {
        return this.crud("CreateDepartment", Map.of("department", dto), new ParameterizedTypeReference<DepartmentResponseDTO>() {
        });
    }

    public Mono<DepartmentResponseDTO> updateDepartment(Integer id, DepartmentRequestDTO dto) {
        return this.crud("UpdateDepartment", Map.of("id", id, "department", dto), new ParameterizedTypeReference<DepartmentResponseDTO>() {
        });
    }

    public Mono<DeleteResponseDTO> deleteDepartment(Integer id) {
        return this.crud("DeleteDepartment", Map.of("id", id), new ParameterizedTypeReference<DeleteResponseDTO>() {
        });
    }


    private <T> Mono<T> crud(String operationName, Map<String, Object> variables, ParameterizedTypeReference<T> type) {
        return this.client.documentName("crud-operations")
                .operationName(operationName)
                .variables(variables)
                .retrieve("response")
                .toEntity(type);
    }
}
