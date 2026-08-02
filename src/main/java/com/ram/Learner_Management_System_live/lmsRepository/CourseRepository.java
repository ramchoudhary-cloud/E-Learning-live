package com.ram.Learner_Management_System_live.lmsRepository;

import com.ram.Learner_Management_System_live.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
