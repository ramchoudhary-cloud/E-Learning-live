package com.ram.Learner_Management_System_live.lmsController.v1;

import com.ram.Learner_Management_System_live.entity.Cohort;
import com.ram.Learner_Management_System_live.lmsService.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CohortV1Controller {

    @Autowired
    private LearnerService _learnerService;

    @GetMapping("/v1/cohorts")
    public Page<Cohort> getPaginatedAndSortedCohorts(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortDir" , defaultValue = "ASC") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "cohortId") String sortBy
    ) {

        if (pageNumber < 0)
            pageNumber = 0;

        if (pageNumber > 100)
            pageNumber = 100;

        if (pageSize < 0)
            pageSize = 10;

        if (pageSize > 100)
            pageSize = 10;

        if (!sortDir.equals("ASC") && !sortDir.equals("DESC"))
            sortDir = "ASC";

        if (!sortBy.equals("cohortId"))
            sortBy = "cohortID";

        return _learnerService.fetchPaginatedAndSortedCohorts(pageNumber, pageSize, sortDir, sortBy);
    }
}
