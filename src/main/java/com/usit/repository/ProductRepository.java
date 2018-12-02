package com.usit.repository;




import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usit.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	
	public Page<Product> findAllByCategoryCdAndProductStatusCd(Pageable pageRequest,String categoryCd,String productStatusCd);
	
	public Page<Product> findAllByBadgeTypeCdAndProductStatusCd(Pageable pageRequest,String badgeTypeCd,String productStatusCd);
	
	public Page<Product> findAllByProductStatusCdNot(Pageable pageRequest,String productDelete);
	
	public Page<Product> findAllBySellMemberIdAndProductStatusCdNot(Pageable pageRequest,int sellMemberId,String productDelete);
	
	public Page<Product> findAllByProductStatusCd(Pageable pageRequest,String productStatusCd);
	
	public Page<Product> findByProductStatusCdOrderByPriceDesc(Pageable pageRequest,String productStatusCd);
	
	public Page<Product> findAllByProductStatusCdAndNewYn(Pageable pageRequest,String productStatusCd,String newYn);
	
	public Page<Product> findAllByProductStatusCdAndPopularYn(Pageable pageRequest,String productStatusCd,String popularYn);
	
	public Page<Product> findAllByProductStatusCdAndLowestYn(Pageable pageRequest,String productStatusCd,String lowestYn);
	
//	public Page<Product> findAllByTempYn(Pageable pageRequest,String tempYn);
	
	public List<Product> findByProductIdIn(List<Integer> productIds);
	
//	@Query("select t from Coupon t where couponNumber=:couponNumber and useYn=:useYn")
//	public Coupon findFirstByCouponNumberAndUseYnSQL(@Param("couponNumber") String couponId, @Param("useYn") String useYn);
//	public Coupon findFirstByCouponNumberAndUseYnAndMemberId(String couponNumber, String useYn, int memeberId);

	

	
}
