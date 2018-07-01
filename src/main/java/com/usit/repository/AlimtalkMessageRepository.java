package com.usit.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usit.domain.AlimtalkMessage;

@Repository
public interface AlimtalkMessageRepository extends JpaRepository<AlimtalkMessage, Integer>{

	public AlimtalkMessage findByTemplateCd(String templateCd);
	
}
