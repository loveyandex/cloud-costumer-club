package com.salinteam.userservice.repository;

import com.salinteam.userservice.model.GroupLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupLevelRepository extends JpaRepository<GroupLevel, Long> {

    Optional<GroupLevel> findGroupLevelById(Long id);


    @Query(value = "select * from group_level where group_level.minscore < ?1 and group_level.maxscore >=?1"
            , nativeQuery = true)
    Optional<GroupLevel> findByScore(Long score);

}
