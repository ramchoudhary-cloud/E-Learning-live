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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long courseID;

    private String courseName;
    private String courseDescription;

    @OneToMany(mappedBy = "course")
    private List<Cohort> cohorts;
}
