package com.ram.Learner_Management_System_live.lmsService;

import com.ram.Learner_Management_System_live.DTO.CohortDTO;
import com.ram.Learner_Management_System_live.DTO.LearnerDTO;
import com.ram.Learner_Management_System_live.entity.Cohort;
import com.ram.Learner_Management_System_live.entity.Learner;
import com.ram.Learner_Management_System_live.exception.CohortNotFoundException;
import com.ram.Learner_Management_System_live.exception.LearnerNotFoundException;
import com.ram.Learner_Management_System_live.lmsRepository.CohortRepository;
import com.ram.Learner_Management_System_live.lmsRepository.LearnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LearnerService {

    @Autowired
    LearnerRepository _learnerRepository;

    @Autowired
    CohortRepository _cohortRepository;

    public Learner createLearner(Learner learner){
        return _learnerRepository.save(learner);
    }

    public List<Learner> getAllLearners() {
        return _learnerRepository.findAll();
    }

    // if below one is unchecked exception then no need to use "throws" anywhere and it will work fine
    // but the add need to handle it via @ExceptionHandler like we do with checked exception with "throws"
    // otherwise response will be not gracefully
    public Learner getById(Long learnerId) throws LearnerNotFoundException {
        Optional<Learner> optionalLearner = _learnerRepository.findById(learnerId);
        if(optionalLearner.isEmpty())
            throw new LearnerNotFoundException("Learner with Id "+ learnerId +" not found");
        return optionalLearner.get();
    }

    public List<Learner> findByName(String learnerName){
        return _learnerRepository.findByLearnerName(learnerName);
    }

    public List<Learner> findByEmail(String learnerEmail){
        return _learnerRepository.findByLearnerEmail(learnerEmail);
    }

    public List<Learner> findByNameAndEmail(String learnerName, String learnerEmail){
        return _learnerRepository.findByLearnerNameAndLearnerEmail(learnerName, learnerEmail);
    }

    public List<Learner> findByRequestParamLogic(String learnerName, String learnerEmail){
        if(learnerName != null &&  learnerEmail != null)
            return findByNameAndEmail(learnerName, learnerEmail);
        if(learnerName == null && learnerEmail == null)
            return getAllLearners(); // is no name then it act as above endpoint line-22 getAllLearners
        if(learnerEmail != null)
            return findByEmail(learnerEmail);
        else
            return findByName(learnerName); // else act as this endpoint with findByName
    }

    public Cohort createCohort(Cohort cohort) {
        return _cohortRepository.save(cohort);
    }

    public Cohort assignLearnerToCohort(Long cohortId, Long learnerId) throws CohortNotFoundException, LearnerNotFoundException{
        Optional<Cohort> optionalCohort = _cohortRepository.findById(cohortId);

        if(optionalCohort.isEmpty()){ // fetch cohort with given Id from cohort table & check whether cohort exist or not
            throw new CohortNotFoundException("cohort with id: "+ cohortId + " not found");
        }

        Optional<Learner> optionalLearner = _learnerRepository.findById(learnerId);

        if(optionalLearner.isEmpty()){ // fetch learner with given Id from learner table & check whether learner exist or not
            throw new LearnerNotFoundException("learner with id: "+ learnerId +"not found");
        }

        Cohort existingCohort = optionalCohort.get();
        for(Learner learner : existingCohort.getLearners()){ // check whether given learner exist in given cohort or not
            if(learner.getLearnerId().equals(learnerId))
                return optionalCohort.get();
        }

        existingCohort.getLearners().add(optionalLearner.get()); // if learner not exist in cohort, add it to cohort
        return _cohortRepository.save(existingCohort);
    }

    public List<Cohort> getAllCohorts() {
        return _cohortRepository.findAll();
    }

    public List<LearnerDTO> learnerTolearnerDTO(List<Learner> learners) {
        List<LearnerDTO> learnerDTOS = new ArrayList<>();

        for(Learner learner : learners){
            LearnerDTO learnerDTO = new LearnerDTO();
            learnerDTO.setLearnerId(learner.getLearnerId());
            learnerDTO.setLearnerName(learner.getLearnerName());
            learnerDTO.setLearnerEmail(learner.getLearnerEmail());
            learnerDTO.setLearnerPhone(learner.getLearnerPhone());
            List<CohortDTO> cohortDTOS = new ArrayList<>();

            for(Cohort cohort : learner.getCohorts()){
                CohortDTO cohortDTO = new CohortDTO();
                cohortDTO.setCohortId(cohort.getCohortId());
                cohortDTO.setCohortName(cohort.getCohortName());
                cohortDTO.setCohortDescription(cohort.getCohortDescription());

                cohortDTOS.add(cohortDTO);
            }
            learnerDTO.setCohortDTOs(cohortDTOS);
            learnerDTOS.add(learnerDTO);
        }
        return learnerDTOS;
    }

//    public LearnerDTO convertLearnerTOLeanerDTO(Learner learner) {
//        LearnerDTO learnerDTO = new LearnerDTO();
//        learnerDTO.setLearnerId(learner.getLearnerId());
//        learnerDTO.setLearnerName(learner.getLearnerName());
//        learnerDTO.setLearnerEmail(learner.getLearnerEmail());
//        learnerDTO.setLearnerPhone(learner.getLearnerPhone());
//
//        return learnerDTO;
//    }
}
