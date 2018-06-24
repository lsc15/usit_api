package com.usit.repository;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usit.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	
	public Page<Product> findAllByCategoryCdAndUseYnAndTempYn(Pageable pageRequest,String categoryCd,String useYn,String tempYn);
	
	public Page<Product> findAllByRegIdAndDeleteYn(Pageable pageRequest,Long regId,String deleteYn);
	
	public Page<Product> findAllByUseYnAndTempYn(Pageable pageRequest,String useYn,String tempYn);
	
	public Page<Product> findAllByTempYn(Pageable pageRequest,String tempYn);
	
//	public List<Coupon> findByMemberId(int memberId);
	
//	@Query("select t from Coupon t where couponNumber=:couponNumber and useYn=:useYn")
//	public Coupon findFirstByCouponNumberAndUseYnSQL(@Param("couponNumber") String couponId, @Param("useYn") String useYn);
//	public Coupon findFirstByCouponNumberAndUseYnAndMemberId(String couponNumber, String useYn, int memeberId);

	

	
}
