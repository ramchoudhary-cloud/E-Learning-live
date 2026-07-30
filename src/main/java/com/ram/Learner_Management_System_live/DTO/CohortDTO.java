package com.ram.Learner_Management_System_live.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CohortDTO {
    private Long cohortId;
    private String cohortName;
    private String cohortDescription;
}
