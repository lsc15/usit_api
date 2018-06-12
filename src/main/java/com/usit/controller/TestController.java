package com.usit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.security.extend.FilterMetadataSource;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Member;
import com.usit.service.TestService;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController extends CommonHeaderController{

    @Autowired
    private TestService testService;

    @Autowired
    private FilterMetadataSource filterMetadataSource;

    @Autowired
    private Environment env;

//    @Value("${spring.jpa.database}")
//    private String apiUrlForToken;

    /**
     * 테스트 컨트롤러(URL: /test/user/1)
     * @param request
     * @param params
     * @param seq
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/refresh-resource-roles", method=RequestMethod.GET)
    public ModelAndView member(HttpServletRequest request, @RequestParam Map<String,Object> params) throws Exception{

        ModelAndView mav = new ModelAndView("jsonView");

        logger.debug("apiUrlForToken:{}", env.getProperty("sms.infobank.rest.url_for_token"));

        //CommonHeaderController를 상속하면 사용가능.
        SignedMember userInfo = getSignedMember(); // 로그인한 사용자의 정보를 담고 있는 객체

        SessionVO sessionVO = userInfo.getMemberInfo(); // 로그인한 사용자의 정보로 부터 상세정보 받아옴

        logger.debug("params:{}", params.toString());
        logger.debug("sessionVO.getMemberId:{}", sessionVO.getMemberId()); // 로그인한 사용자의 사용자고유 id
        logger.debug("sessionVO.getMemberEmail:{}", sessionVO.getMemberEmail()); // 로그인한 사용자의 이메일주소
        logger.debug("sessionVO.getMemberName:{}", sessionVO.getMemberName()); // 로그인한 사용자의 이름

        Map<String, Object> resultData = new HashMap<String, Object>();
        String resultCode = "0000";
        String resultMsg = "";

        try {

            filterMetadataSource.reload(); // loads roles of resources again.

        }catch(FrameworkException e){
            logger.error("CommFrameworkException", e);
            resultCode = e.getMsgKey();
            resultMsg = e.getMsg();
        }catch(Exception e){
            logger.error("Exception", e);
            resultCode = "-9999";
            resultMsg = "처리중 오류가 발생하였습니다.";
        }

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", resultData);

        return mav;
    }


}
