package com.ram.Learner_Management_System_live.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Learner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long learnerId;
    private String learnerName;
    private String learnerEmail;
    private String learnerPhone;

    // if we do not use mappedBy is will create two tables of cohort_learner
    @ManyToMany(mappedBy = "learners") // this mappedBy side become Back-Referencing relation
    @JsonIgnore
    private List<Cohort> cohorts;


}
