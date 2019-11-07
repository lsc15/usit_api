package com.usit.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usit.domain.UsitEmailContent;

@Repository
public interface UsitEmailContentRepository extends JpaRepository<UsitEmailContent, Integer>{


}
