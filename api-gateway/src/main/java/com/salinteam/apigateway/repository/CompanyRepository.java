package com.salinteam.apigateway.repository;

import com.salinteam.apigateway.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {


    Optional<Company> findByUsername(String username);

    Boolean existsByUsername(String username);

}
