package com.salinteam.eventservice.repository;

import com.salinteam.eventservice.model.CoEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */

@Repository
public interface CoEventRepository extends JpaRepository<CoEvent, Long> {

    Optional<CoEvent> findCoEventByCompanyIdAndAndEventIdAndGroupLevelId(Long companyId, String eventId, Long glId);

    List<CoEvent> findCoEventsByEventId(String eventId);

}
