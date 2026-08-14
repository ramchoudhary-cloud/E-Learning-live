package com.ram.Learner_Management_System_live.lmsController;

import com.ram.Learner_Management_System_live.DTO.LearnerList;
import com.ram.Learner_Management_System_live.entity.Cohort;
import com.ram.Learner_Management_System_live.entity.ErrorResponse;
import com.ram.Learner_Management_System_live.entity.Learner;
import com.ram.Learner_Management_System_live.exception.CohortNotFoundException;
import com.ram.Learner_Management_System_live.exception.LearnerNotFoundException;
import com.ram.Learner_Management_System_live.lmsService.LearnerService;
import jakarta.validation.Valid;
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
    // (1) basic noob way to design API endpoint with query parameters,below API is optimal
//    @PostMapping("/assignLearnerToCohort")
//    public Cohort assignLearnerToCohort(@RequestParam("cohortId") Long cohortId, @RequestParam("learnerId") Long learnerId) throws CohortNotFoundException, LearnerNotFoundException {
//        return _learnerService.assignLearnerToCohort(cohortId, learnerId);
//    }

    // (2) optimal way to design API endpoint via parent/chaild relationship
//    @PostMapping("/cohorts/{cohortId}/learners")
//    public Cohort mapLearnerToCohort(@PathVariable("cohortId") Long cohortId, @RequestBody LearnerList learnerList)throws CohortNotFoundException{
//    return _learnerService.mapLearnerToCohort(cohortId, learnerList.getLearnerList()); // wrapper class LearnerList
//    }
    // Above taking list of learnerId's in body...we are taking it in wrapper LearnerList class that we have created
    // we can also use direct List<Long> learneIds;....instead

    // (3) Creating and Mapping learners to Cohort in single endpoint
    @PostMapping("/cohorts/{cohortId}/learners")
    public Cohort createLearnerAndMapToCohort(@PathVariable("cohortId") Long cohortId, @Valid @RequestBody List<Learner> learners) throws CohortNotFoundException {
        return _learnerService.createLearnerAndMapToCohort(cohortId, learners);
    }
}
