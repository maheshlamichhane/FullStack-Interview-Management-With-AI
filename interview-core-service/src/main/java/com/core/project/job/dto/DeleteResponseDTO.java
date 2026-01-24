package com.core.project.job.dto;


import com.core.project.job.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class DeleteResponseDTO {

    private Long id;
    private Status status;

}
