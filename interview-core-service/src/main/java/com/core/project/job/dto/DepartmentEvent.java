package com.core.project.job.dto;

import com.core.project.job.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class DepartmentEvent {

    private Long id;
    private Action action;

}
