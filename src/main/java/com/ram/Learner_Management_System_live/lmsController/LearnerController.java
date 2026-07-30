package com.ram.Learner_Management_System_live.lmsController;

import com.ram.Learner_Management_System_live.DTO.LearnerDTO;
import com.ram.Learner_Management_System_live.entity.ErrorResponse;
import com.ram.Learner_Management_System_live.entity.Learner;
import com.ram.Learner_Management_System_live.exception.LearnerNotFoundException;
import com.ram.Learner_Management_System_live.lmsService.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class LearnerController {
    // create endpoints

    @Autowired
    LearnerService _learnerService;

    // curl -X POST http://localhost:1002/learners -d '{"learnerName":"Ram","learnerEmail":"email","learnerPhone":"123"}' -H "Content-Type:application/json"
    @PostMapping("/learners")
    public Learner createLearner(@RequestBody Learner learner){
        return _learnerService.createLearner(learner);
    }

//    @GetMapping("/learners")
//    public List<Learner> getAllLearners(){
//        return _learnerService.getAllLearners();
//    }

    @GetMapping("/learners/{learnerId}") // path parameter -> {learnerId}
    public Learner getLearnerById(@PathVariable("learnerId") Long learnerId) throws LearnerNotFoundException {
        return _learnerService.getById(learnerId);
        //return _learnerService.convertLearnerTOLeanerDTO(learner);
    }

    // this act as two endpoints above as well this..getAllLearners / findByName
    // because @RequestParam allow multiple request parameters with optional fields
    // required = fales...make fields optinal
    @GetMapping("/learners")
    public List<LearnerDTO> findByName(@RequestParam(value = "learnerName", required = false) String learnerName,
                                       @RequestParam(value = "learnerEmail", required = false) String learnerEmail){
        List<Learner> learners = _learnerService.findByRequestParamLogic(learnerName, learnerEmail);

        return _learnerService.learnerTolearnerDTO(learners);
    }
    //we modeled single endpoint to do two things with the help of optional field provided by @RequestParam
    // we writing business logic in rest-controller so further code optimization we can write this logic code in service layer and apply design principles
    // and make rest-controller abstract of how endpoint points work behind the scene


    // Standard Response
//    @ExceptionHandler(LearnerNotFoundException.class) // useful for dispatcher servelet, it will handle exception
//    public ResponseEntity handleLearnerNotFoundException(LearnerNotFoundException e){
//        return ResponseEntity.status(404).body(e.getMessage());
//        // body(e.getStackTrace())....to see all details where it happens, to where it propogate
//    }

    // Custom Response
    @ExceptionHandler(LearnerNotFoundException.class)
    public ErrorResponse handleLearnerNotFoundException(LearnerNotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), new Date().toInstant().toEpochMilli(), ex.getStackTrace().toString());
    }

}
