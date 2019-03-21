package com.usit.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.DateUtil;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Member;
import com.usit.domain.PointHistory;
import com.usit.domain.ShareHistory;
import com.usit.domain.UsitOrderItem;
import com.usit.service.MemberService;
import com.usit.service.PointHistoryService;
import com.usit.service.ShareHistoryService;
import com.usit.util.TimeUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point-histories")
public class PointHistoryController extends CommonHeaderController{

	private static Logger LOGGER = LoggerFactory.getLogger(PointHistoryController.class);

	@Autowired
	PointHistoryService pointHistoryService;
 
	@Autowired
    ShareHistoryService shareHistoryService;

	@Autowired
	MemberService memberService;
	
	@Autowired
    private Environment env;
	
	
	@PostMapping
	public ModelAndView addPoint(@RequestBody PointHistory point) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";

     	SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴
        
     	point.setRegId(sessionVO.getMemberId());
     	PointHistory result = new PointHistory();
     	result = pointHistoryService.addPoint(point);
        
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", result);
		
		 return mav;
	}
	

	
	@GetMapping()
	public ModelAndView getPointList(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "pointId"));

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴

        
        Page<PointHistory> page = pointHistoryService.getPointListByMemberId(pageRequest,sessionVO.getMemberId());
//        List<PointHistory> page = pointHistoryService.getPointListByMemberId2(sessionVO.getMemberId());
        
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
	
	
	@GetMapping("/summary")
	public ModelAndView getPointSummary(@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
		PageRequest pageRequest = new PageRequest(curPage, perPage, new Sort(Direction.DESC, "pointId"));

		ModelAndView mav = new ModelAndView("jsonView");
		
		String resultCode = "0000";
        String resultMsg = "";
        
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

     	SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴

        
        Page<PointHistory> page = pointHistoryService.getPointSummaryByMemberId(pageRequest,sessionVO.getMemberId());
        
		mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", page);
		
		 return mav;
	}
	
	
	
	
	// 추천인 이벤트 초 분 시 일 월 주(년)
	@Scheduled(cron = "0 40 14 * * ?")
	@Transactional
	public void event() throws Exception{
	
		if("real".equals(env.getProperty("running.system"))) {
		logger.info("@@이벤트 시작");
		List<PointHistory> eventList = shareHistoryService.getEventMemberForAddPoint(DateUtil.getCurrDate());
		for (Iterator<PointHistory> iterator = eventList.iterator(); iterator.hasNext();) {
			PointHistory share = (PointHistory) iterator.next();
			//포인트증감이력 저장
			//포인트적용
			Member member = memberService.getMemberByMemeberId(share.getMemberId());
			int present = member.getTotalPoint() + 10000;
			PointHistory point = new PointHistory();
			point.setAddPoint(10000);
			point.setRegId(share.getMemberId());
			point.setBalancePoint(present);
			point.setAddPct(0);
			point.setPointTypeCd(UsitCodeConstants.POINT_TYPE_CD_EVENT);	
			point.setMemberId(share.getMemberId());
			pointHistoryService.addPointEvent(point);
		
		}
		}
	}
	
	
	
//	@Scheduled(cron = "0 0 3,9,13 * * ?")
//    public void updateDeliveryStatus() throws Exception{
//    	if("real".equals(env.getProperty("running.system"))) {
//	   logger.info("@@배송상태동기화 시작");
//	   List<String> orderItemStatusCd = new ArrayList<String>();
//	   orderItemStatusCd.add("1203");
//	   orderItemStatusCd.add("1206");
//	   List<UsitOrderItem> checkList = pointHistoryService.getPointListByMemberId(page, member)(orderItemStatusCd);
//    	  
//    	for (Iterator<UsitOrderItem> iterator = checkList.iterator(); iterator.hasNext();) {
//    		UsitOrderItem usitOrderItem = (UsitOrderItem) iterator.next();
//			JSONObject result = commonService.checkTracker(usitOrderItem.getTrackingNumber(),usitOrderItem.getDeliveryCompanyCd());
//			if("Y".equals(result.get("completeYN"))) {
//				if("1203".equals(usitOrderItem.getDeliveryStatusCd())){
//					logger.info("@@equals 1203: ");
//					usitOrderItem.setDeliveryStatusCd("1204");
//					usitOrderItem.setDeliveryCompleteDate(TimeUtil.getZonedDateTimeNow("Asia/Seoul"));
//				}else if("1206".equals(usitOrderItem.getDeliveryStatusCd())){
//					logger.info("@@equals 1206: ");
//					usitOrderItem.setDeliveryStatusCd("1207");
//				}else {
//					//추후변경분
//				}
//				logger.info("@@배송상태동기화 배송번호: "+ usitOrderItem.getTrackingNumber());
//				orderItemService.setOrderItemTracker(usitOrderItem);
//				logger.info("@@save ");
//				logger.info("@@status: " + usitOrderItem.getDeliveryStatusCd());
//				Thread.sleep(1000);
//			}
//		}
//    	
//    
//    	}
//    }
	
	
	
	
	
	
	



}
