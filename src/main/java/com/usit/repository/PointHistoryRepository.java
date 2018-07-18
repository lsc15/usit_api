package com.usit.repository;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usit.domain.PointHistory;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer>{

	
	public Page<PointHistory> findAllByMemberId(Pageable Pageable,int memberId);

}
