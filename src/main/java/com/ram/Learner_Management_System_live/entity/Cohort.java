package com.ram.Learner_Management_System_live.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cohort {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cohortId;
    private String cohortName;
    private String cohortDescription;

    @ManyToMany  // this side become OWNER relation
    private List<Learner> learners;


}
