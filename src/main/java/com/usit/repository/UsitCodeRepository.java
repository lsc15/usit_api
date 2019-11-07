package com.usit.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usit.domain.UsitCode;

@Repository
public interface UsitCodeRepository extends JpaRepository<UsitCode, String>{


    public List<UsitCode> findByMasterCd(String masterCd);

}
