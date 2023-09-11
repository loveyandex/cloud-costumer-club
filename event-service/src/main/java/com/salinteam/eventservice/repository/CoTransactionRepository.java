package com.salinteam.eventservice.repository;


import com.salinteam.eventservice.model.CoTransaction;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */

@Repository
public interface CoTransactionRepository extends JpaRepository<CoTransaction, Long> {
    Optional<CoTransaction> findCoTransactionByCompanyIdAndTransactionIdAndGroupLevelId(Long companyId, String eventId, Long glId);


}
