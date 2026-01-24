package com.core.project.job.controller;

import com.core.project.job.dto.DeleteResponseDTO;
import com.core.project.job.dto.DepartmentRequestDTO;
import com.core.project.job.enums.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

@SpringBootTest
@AutoConfigureHttpGraphQlTester
public class GraphqlCrudTest {

    @Autowired
    private HttpGraphQlTester client;

    @Test
    public void allDepartmentTest(){
        var doc = """
					query{
						getAllDepartments{
							id
							name
							code
						}
					}
				""";
        this.client.document(doc)
                .execute()
                .path("getAllDepartments").entityList(Object.class).hasSizeGreaterThan(2)
                .path("getAllDepartments.[0].name").entity(String.class).isEqualTo("IT Product Department 2");
    }


    @Test
    public void departmentByIdTest(){
        this.client.documentName("crud-operations")
                .variable("id", 2)
                .operationName("GetDepartmentById")
                .execute()
                .path("response.id").entity(Integer.class).isEqualTo(2)
                .path("response.name").entity(String.class).isEqualTo("IT Product Department 2")
                .path("response.code").entity(String.class).isEqualTo("PD-IT");
    }

    @Test
    public void createDepartmentTest(){

        DepartmentRequestDTO department = new DepartmentRequestDTO();
        department.setName("wow2");
        department.setCode("PD-IT-002");
        department.setDescription("Responsible for product development, research, and innovation across all platforms");
        department.setManagerId(12L);
        department.setActive(true);
        department.setBudgetCode("PD-Q2-2024");
        department.setCostCenter("CC-PD-002");

        this.client.documentName("crud-operations")
                .variable("department", department)
                .operationName("CreateDepartment")
                .execute()
                .path("response.id").entity(Integer.class).isEqualTo(12)
                .path("response.name").entity(String.class).isEqualTo("wow2")
                .path("response.code").entity(String.class).isEqualTo("PD-IT-002");
    }


    @Test
    public void updateDepartmentTest(){

        DepartmentRequestDTO department = new DepartmentRequestDTO();
        department.setName("wow2");
        department.setCode("PD-IT-002");
        department.setDescription("Responsible for product development, research, and innovation across all platforms");
        department.setManagerId(20L);
        department.setActive(true);
        department.setBudgetCode("PD-Q2-2025");
        department.setCostCenter("CC-PD-002");

        this.client.documentName("crud-operations")
                .variable("id", 12)
                .variable("department", department)
                .operationName("UpdateDepartment")
                .execute()
                .path("response.id").entity(Integer.class).isEqualTo(12)
                .path("response").entity(Object.class).satisfies(System.out::println);
    }

    @Test
    public void deleteDepartmentTest(){
        this.client.documentName("crud-operations")
                .variable("id", 5)
                .operationName("DeleteDepartment")
                .execute()
                .path("response").entity(DeleteResponseDTO.class).satisfies(r -> {
                    Assertions.assertThat(r.getId()).isEqualTo(5);
                    Assertions.assertThat(r.getStatus()).isEqualTo(Status.SUCCESS);
                });
    }
}
