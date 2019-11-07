package com.usit.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usit.domain.UsitEmail;

@Repository
public interface UsitEmailRepository extends JpaRepository<UsitEmail, Integer>{


    public List<UsitEmail> findTop1000BySendDateGreaterThanAndSendStatus(String sendDage,String sendStatus);
    
    public Page<UsitEmail> findBySendDate(String sendDate,Pageable pageable);
    public Page<UsitEmail> findBySendDateAndSendStatus(String sendDate,String sendStatus,Pageable pageable);

}
