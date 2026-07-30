package com.ram.Learner_Management_System_live.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LearnerDTO {
    private Long learnerId;
    private String learnerName;
    private String learnerEmail;
    private String learnerPhone;

    private List<CohortDTO> cohortDTOs;
}
