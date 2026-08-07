package com.ram.Learner_Management_System_live.lmsRepository;

import com.ram.Learner_Management_System_live.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearnerRepository extends JpaRepository<Learner, Long> {

    public List<Learner> findByLearnerName(String learnerName);

    public Optional<Learner> findByLearnerEmail(String learnerEmail);

    public List<Learner> findByLearnerNameAndLearnerEmail(String learnerName, String learnerEmail);
}
