package com.usit.repository;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usit.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	
	public Page<Product> findAllByCategoryCdAndUseYn(Pageable pageRequest,String categoryCd,String useYn);
	
	public Page<Product> findAllByRegIdAndDeleteYn(Pageable pageRequest,String regId,String deleteYn);
	
	public Page<Product> findAllByUseYn(Pageable pageRequest,String useYn);
	
//	public List<Coupon> findByMemberId(int memberId);
	
//	@Query("select t from Coupon t where couponNumber=:couponNumber and useYn=:useYn")
//	public Coupon findFirstByCouponNumberAndUseYnSQL(@Param("couponNumber") String couponId, @Param("useYn") String useYn);
//	public Coupon findFirstByCouponNumberAndUseYnAndMemberId(String couponNumber, String useYn, int memeberId);

	

	
}
