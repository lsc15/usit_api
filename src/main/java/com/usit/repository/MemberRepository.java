package com.usit.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usit.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>{

	public Member findByMemberUid(String memberUid);
	
	public Member findByEmail(String email);

	public Page<Member> findAll(Specification<Member> spec,Pageable pageRequest );

}