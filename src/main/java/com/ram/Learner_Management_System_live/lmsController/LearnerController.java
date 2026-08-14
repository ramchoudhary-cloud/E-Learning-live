package com.ram.Learner_Management_System_live.lmsController;

import com.ram.Learner_Management_System_live.DTO.LearnerDTO;
import com.ram.Learner_Management_System_live.entity.ErrorResponse;
import com.ram.Learner_Management_System_live.entity.Learner;
import com.ram.Learner_Management_System_live.exception.LearnerNotFoundException;
import com.ram.Learner_Management_System_live.lmsService.LearnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LearnerController {
    // create endpoints

    @Autowired
    LearnerService _learnerService;

    // curl -X POST http://localhost:1002/learners -d '{"learnerName":"Ram","learnerEmail":"email","learnerPhone":"123"}' -H "Content-Type:application/json"
    @PostMapping("/learners")
    public Learner createLearner(@Valid  @RequestBody Learner learner){
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

    @DeleteMapping("/learners/{learnerId}")
    public ResponseEntity deleteLearner(@PathVariable("learnerId") Long learnerId) throws LearnerNotFoundException{
        _learnerService.deleteLearner(learnerId);

        return ResponseEntity.noContent().build();
    }

    // this act as two endpoints above as well this..getAllLearners / findByName
    // because @RequestParam allow multiple request parameters with optional fields
    // required = fales...make fields optinal
    @GetMapping("/learners")
    public List<LearnerDTO> findByNameOrEmail(@RequestParam(value = "learnerName", required = false) String learnerName,
                                       @RequestParam(value = "learnerEmail", required = false) String learnerEmail)throws LearnerNotFoundException{
        List<Learner> learners = _learnerService.findByNameOrEmail(learnerName, learnerEmail);

        return _learnerService.learnerTolearnerDTO(learners);
    }
    //we modeled single endpoint to do two things with the help of optional field provided by @RequestParam
    // we writing business logic in rest-controller so further code optimization we can write this logic code in service layer and apply design principles
    // and make rest-controller abstract of how endpoint points work behind the scene


}
