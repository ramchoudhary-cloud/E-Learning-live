package com.ram.Learner_Management_System_live.lmsService;

import com.ram.Learner_Management_System_live.DTO.CohortDTO;
import com.ram.Learner_Management_System_live.DTO.LearnerDTO;
import com.ram.Learner_Management_System_live.entity.Cohort;
import com.ram.Learner_Management_System_live.entity.Course;
import com.ram.Learner_Management_System_live.entity.Learner;
import com.ram.Learner_Management_System_live.exception.CohortNotFoundException;
import com.ram.Learner_Management_System_live.exception.LearnerNotFoundException;
import com.ram.Learner_Management_System_live.lmsRepository.CohortRepository;
import com.ram.Learner_Management_System_live.lmsRepository.CourseRepository;
import com.ram.Learner_Management_System_live.lmsRepository.LearnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    CourseRepository _courseRepository;

    public Learner createLearner(Learner learner){
        return _learnerRepository.save(learner);
    }

    public List<Learner> getAllLearners() {
        return _learnerRepository.findAll();
    }

    // if below one is unchecked exception then no need to use "throws" anywhere and it will work fine
    // but the add need to handle it via @ExceptionHandler like we do with checked exception with "throws"
    // otherwise response will be not gracefully

    //@Cacheable(value = "learners", key = "#learnerId")
    public Learner getById(Long learnerId) throws LearnerNotFoundException {
        Optional<Learner> optionalLearner = _learnerRepository.findById(learnerId);
        if(optionalLearner.isEmpty())
            throw new LearnerNotFoundException("learner with id "+ learnerId +" not found");
        return optionalLearner.get();
    }

    //@CacheEvict(value = "learners", key = "#learnerId")
    public void deleteLearner(Long learnerId) throws LearnerNotFoundException{
        if(! _learnerRepository.existsById(learnerId))
            throw new LearnerNotFoundException("learner with id: "+ learnerId+"not found");

        _learnerRepository.deleteById(learnerId);
    }

    public List<Learner> findByName(String learnerName) throws LearnerNotFoundException{
         List<Learner> learnerList =  _learnerRepository.findByLearnerName(learnerName);
         if(learnerList.isEmpty()){
             throw new LearnerNotFoundException("learner with name: " +learnerName +"not found");
         }
         return learnerList;
    }

    public Optional<Learner> findByEmail(String learnerEmail) throws LearnerNotFoundException{
        Optional<Learner> learnerOptional =  _learnerRepository.findByLearnerEmail(learnerEmail);
         if(learnerOptional.isEmpty()){
             throw new LearnerNotFoundException("learner with email: "+ learnerEmail +"not found");
         }
         return learnerOptional;
    }

    public List<Learner> findByNameAndEmail(String learnerName, String learnerEmail){
        return _learnerRepository.findByLearnerNameAndLearnerEmail(learnerName, learnerEmail);
    }

    public List<Learner> findByNameOrEmail(String learnerName, String learnerEmail)throws LearnerNotFoundException{
        if(learnerName != null &&  learnerEmail != null)
            return findByNameAndEmail(learnerName, learnerEmail);
        if(learnerName == null && learnerEmail == null)
            return getAllLearners(); // is no name then it act as above endpoint line-22 getAllLearners
        if(learnerEmail != null)
            return findByEmail(learnerEmail).stream().toList();
        else
            return findByName(learnerName); // else act as this endpoint with findByName
    }

    public Cohort createCohort(Cohort cohort) {
        return _cohortRepository.save(cohort);
    }

//    (1) basic noob way
//    public Cohort assignLearnerToCohort(Long cohortId, Long learnerId) throws CohortNotFoundException, LearnerNotFoundException{
//        Optional<Cohort> optionalCohort = _cohortRepository.findById(cohortId);
//        if(optionalCohort.isEmpty()) { // fetch cohort with given Id from cohort table & check whether cohort exist or not
//            throw new CohortNotFoundException("cohort with id: " + cohortId + " not found");
//        }
//        Optional<Learner> optionalLearner = _learnerRepository.findById(learnerId);
//
//        if(optionalLearner.isEmpty()){ // fetch learner with given Id from learner table & check whether learner exist or not
//            throw new LearnerNotFoundException("learner with id: "+ learnerId +"not found");
//        }
//        Cohort existingCohort = optionalCohort.get();
//        for(Learner learner : existingCohort.getLearners()){ // check relationship whether given learner exist in given cohort or not
//            if(learner.getLearnerId().equals(learnerId))
//                return optionalCohort.get();
//        }
//        existingCohort.getLearners().add(optionalLearner.get()); // if learner not exist in cohort, add it to cohort
//        return _cohortRepository.save(existingCohort);
//    }

    //(2) optimal way
    public Cohort mapLearnerToCohort(Long cohortId, List<Long> learnerIds)throws CohortNotFoundException{
        Optional<Cohort> optionalCohort = _cohortRepository.findById(cohortId);
        if(optionalCohort.isEmpty())
            throw new CohortNotFoundException("cohort with id: "+ cohortId + "not found");

        Cohort cohortObj = optionalCohort.get();

        for(Long learnerId : learnerIds) {
            Optional<Learner> optionalLearner = _learnerRepository.findById(learnerId);
            if(optionalLearner.isPresent()){
                Learner learnerObj = optionalLearner.get();
                for(Learner mappedLearner : cohortObj.getLearners()){
                    if(mappedLearner.getLearnerId().equals(learnerObj.getLearnerId()))
                        break;
                }
                cohortObj.getLearners().add(learnerObj);
            }
        }
        return _cohortRepository.save(cohortObj);
    }

    // (3) Single API Creating and Mapping learners to Cohort simultaneously
    public Cohort createLearnerAndMapToCohort(Long cohortId, List<Learner> learners) throws CohortNotFoundException{
        Optional<Cohort> optionalCohort = _cohortRepository.findById(cohortId);
        if(optionalCohort.isEmpty())
            throw new CohortNotFoundException("cohort with id: "+ cohortId + "not found");

        Cohort cohortObj = optionalCohort.get();
        for(Learner learner : learners){
            Optional<Learner> optionalLearner = _learnerRepository.findByLearnerEmail(learner.getLearnerEmail());
            if(optionalLearner.isEmpty()){
                _learnerRepository.save(learner);
                cohortObj.getLearners().add(learner);
            }
        }
        return _cohortRepository.save(cohortObj);
    }

    public List<Cohort> getAllCohorts() {
        return _cohortRepository.findAll();
    }

    public Page<Cohort> fetchPaginatedAndSortedCohorts(int pageNumber, int pageSize, String sortDir, String sortBy){
        Sort.Direction direction;
        if(sortDir.equals("ASC"))
            direction = Sort.Direction.ASC;
        else direction = Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, direction, sortBy);
        return _cohortRepository.findAll(pageable);
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

    public Course createCourse(Course course){
        return _courseRepository.save(course);
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
