package com.usit.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.usit.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>{

	public Member findByMemberUid(String memberUid);
	
	public Member findByEmail(String email);

	public Page<Member> findAll(Specification<Member> spec,Pageable pageRequest );
	
	
	
	
//	@Query(value = "select re.order_item_id, ref.product_id, ref.phenotype, ref.grade from theragen_result re, nutrient_reference ref where re.phenotype_id = ref.phenotype and re.result = ref.grade and order_item_id =:orderItemId order by result desc,rand()", nativeQuery = true)
	
	@Query(value = "SELECT t2.member_id as lev2_id, t2.name as lev2, t3.member_id as lev3_id,t3.name as lev3,t4.member_id as lev4_id, t4.name as lev4"
	+ " FROM member AS t1"
	+ " LEFT JOIN member AS t2 ON t2.first_recommender = t1.member_id"
	+ " LEFT JOIN member AS t3 ON t3.first_recommender = t2.member_id"
	+ " LEFT JOIN member AS t4 ON t4.first_recommender = t3.member_id"
	+ " where t1.member_id = :memberId", nativeQuery = true)
	public List<Object[]> findRecommenderList(@Param("memberId") int memberId);

}