package com.usit.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.VerifyToken;

@Repository
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Integer>{

    public VerifyToken findByEmailAndToken(String email,String token);
    public VerifyToken findFirstByMobileOrderByVerifyTokenIdDesc(String mobile);
}
