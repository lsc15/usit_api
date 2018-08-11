package com.usit.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer>{


    public List<Content> findByAuthor(String author);
    public Page<Content> findByUseYnAndContentTypeCd(String useYn,String contentTypeCd, Pageable pageable);
    public Page<Content> findByContentTypeCd(String contentTypeCd, Pageable pageable);

}
