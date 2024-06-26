package com.inncretech.linkedin.Repository;

import com.inncretech.linkedin.Models.Company_followers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;


public interface CompanyFollowerRepository extends JpaRepository<Company_followers,Long> {

    Optional<Company_followers> findByCompanyIdAndUserId(Long companyId, Long userId);
    List<Company_followers> findByCompanyId(Long companyId);
}
