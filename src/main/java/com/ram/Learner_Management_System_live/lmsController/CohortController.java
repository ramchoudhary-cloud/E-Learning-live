package com.ram.Learner_Management_System_live.lmsController;

import com.ram.Learner_Management_System_live.entity.Cohort;
import com.ram.Learner_Management_System_live.entity.ErrorResponse;
import com.ram.Learner_Management_System_live.exception.CohortNotFoundException;
import com.ram.Learner_Management_System_live.exception.LearnerNotFoundException;
import com.ram.Learner_Management_System_live.lmsService.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class CohortController {

    @Autowired
    private LearnerService _learnerService;

    // curl -X POST http://localhost:1002/cohorts -d '{"cohortName":"C1","cohortDescription":" cohort_C1"}' -H "Content-Type:application/json"
    // when initial create cohort....then there might not be any learner enrolled so as of now we not provide learner and JPA map learner to null
    @PostMapping("/cohorts")
    public Cohort createCohorts(@RequestBody Cohort cohort){
        return _learnerService.createCohort(cohort);
    }

    @GetMapping("/cohorts")
    public List<Cohort> getAllCohorts(){
        return _learnerService.getAllCohorts();
    }
    // basic noob way to design API endpoint with query parameters
    @PostMapping("/assignLearnerToCohort")
    public Cohort assignLearnerToCohort(@RequestParam("cohortId") Long cohortId, @RequestParam("learnerId") Long learnerId) throws CohortNotFoundException, LearnerNotFoundException {
        return _learnerService.assignLearnerToCohort(cohortId, learnerId);
    }

    @ExceptionHandler(LearnerNotFoundException.class)
    public ErrorResponse handleLearnerNotFoundException(LearnerNotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), new Date().toInstant().toEpochMilli(), ex.getStackTrace().toString());
    }

    @ExceptionHandler(CohortNotFoundException.class)
    public ErrorResponse handleCohortNotFoundException(LearnerNotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), new Date().toInstant().toEpochMilli(), ex.getStackTrace().toString());
    }
}
