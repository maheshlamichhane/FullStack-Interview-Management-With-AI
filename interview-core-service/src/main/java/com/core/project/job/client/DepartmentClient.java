package com.core.project.job.client;

import com.core.project.job.dto.DepartmentRequestDTO;
import com.core.project.job.service.DepartmentClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepartmentClient implements CommandLineRunner {

    @Autowired
    private DepartmentClientService departmentClientService;

    @Override
    public void run(String... args) {

        departmentClientService.deleteDepartment(9)
                .subscribe(
                        response -> {
                            System.out.println("✅ Response received:");
                            System.out.println(response);
                        },
                        error -> {
                            System.err.println("❌ Error occurred:");
                            error.printStackTrace();
                        }
                );

//        DepartmentRequestDTO department = new DepartmentRequestDTO();
//
//        department.setName("Health");
//        department.setCode("PD-IT 04d");
//        department.setDescription("Responsible for product development, research, and innovation across all platforms");
//        department.setManagerId(12L);
//        department.setActive(true);
//        department.setBudgetCode("PD-Q2-2024");
//        department.setCostCenter("CC-PD-002");


//        departmentClientService.updateDepartment(9,department)
//                .subscribe(
//                        response -> {
//                            System.out.println("✅ Response received:");
//                            System.out.println(response);
//                        },
//                        error -> {
//                            System.err.println("❌ Error occurred:");
//                            error.printStackTrace();
//                        }
//                );


//        DepartmentRequestDTO department = new DepartmentRequestDTO();
//
//        department.setName("zzzdsfdsdsdfsss");
//        department.setCode("PD-IT 04d");
//        department.setDescription("Responsible for product development, research, and innovation across all platforms");
//        department.setManagerId(12L);
//        department.setActive(true);
//        department.setBudgetCode("PD-Q2-2024");
//        department.setCostCenter("CC-PD-002");
//        department.setCreatedAt(LocalDateTime.of(2026, 1, 24, 10, 0, 0));
//        department.setUpdatedAt(LocalDateTime.of(2026, 1, 24, 10, 0, 0));



//        departmentClientService.createDepartment(department)
//                .subscribe(
//                        response -> {
//                            System.out.println("✅ Response received:");
//                            System.out.println(response);
//                        },
//                        error -> {
//                            System.err.println("❌ Error occurred:");
//                            error.printStackTrace();
//                        }
//                );

//        departmentClientService.allDepartments()
//                .subscribe(
//                        response -> {
//                            System.out.println("✅ Response received:");
//                            System.out.println(response);
//                        },
//                        error -> {
//                            System.err.println("❌ Error occurred:");
//                            error.printStackTrace();
//                        }
//                );
    }
}

//        departmentClientService.getDepartmentById(3)
//                .subscribe(
//                        response -> {
//                            System.out.println("✅ Response received:");
//                            System.out.println(response);
//                        },
//                        error -> {
//                            System.err.println("❌ Error occurred:");
//                            error.printStackTrace();
//                        }
//                );
