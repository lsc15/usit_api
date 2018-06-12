package com.usit.repository;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.ResourceRole;

@Repository
public interface ResourceRoleRepository extends JpaRepository<ResourceRole, Integer>{

//    public List<ResourceRole> findByReqUri();
//
//    public List<ResourceRole> findByReqUriAndReqMethod();

}
