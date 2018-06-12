package com.usit.app.spring.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.SessionVO;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.service.LoginService;

@Service
public class UserDetailsService implements AuthenticationUserDetailsService<Authentication> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginService loginService;


    @Override
    public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {

         logger.debug("@@@");
         logger.debug("@@@ loadUserDetails ====================================");
         logger.debug("@@@");

         String userId = (String) authentication.getPrincipal();
         String loginPswd = (String) authentication.getCredentials();

         logger.debug("@@@");
         logger.debug("@@@ Input userId    =[" + userId + "]");
//         logger.debug("@@@ Input Password  =[" + loginPswd + "]");
         logger.debug("@@@");

         SessionVO vo = null;
         try {
        	 if(userId != null) {
        		 
        		 
        		 if(request.getHeader("usit-auth") != null) {
        			 vo = loginService.getLoginMemberByEmail(userId);
        	       }else if(request.getHeader("usitshop-auth") != null) {
        	    	   vo = loginService.getLoginShopMemberByEmail(userId);
        	           }
        		 
        	 }
         } catch (Exception e) {
             e.printStackTrace();
             logger.info("@@@ selectLoginList  Exception=" + e.toString());
             throw new UsernameNotFoundException("로그인 정보 조회 중 오류가 발생하였습니다.");
         }

         if (vo == null) {
             Map<String, Object> logInfoMap = new HashMap<String, Object>();

             logInfoMap.put("loginId", userId);
             logInfoMap.put("loginPassword", loginPswd);

             logger.debug("@@@ request.RemoteAddr=" + request.getRemoteAddr());

             try {
//                 logInfoMap.put("loginContents", "로그인 아이디 없음");
             } catch (Exception e) {
                 e.printStackTrace();
                 logger.info("@@@ loadUserDetails Exception=" + e.toString());
             }

//             throw new UsernameNotFoundException("등록된 사용자ID가 아닙니다. 다시한번 확인해 주세요.");
         }

         if(vo == null || vo.getMemberRoles().size() < 1) { // sets default as 'USIT_USER'
        	 if(vo == null) {
        		 vo = new SessionVO();
        		 vo.setMemberId("");
        		 vo.setMemberName("");
        	 }
             List<String> _roll = new ArrayList<String>();
             _roll.add("USIT_USER");
             vo.setMemberRoles(_roll);
             vo.setPassword("");
         }

         SignedMember signeduser = new SignedMember(vo);

         return signeduser;
    }

    public UserDetails getUserById(String type, String id, String pwd) throws UsernameNotFoundException {

            logger.debug("@@@");
        logger.debug("@@@ getUserKey ====================================");
        logger.debug("@@@");


        logger.debug("@@@");
        logger.debug("@@@ Input id    =[" + id + "]");
        logger.debug("@@@ Input pwd    =[" + pwd + "]");
        logger.debug("@@@");

        SessionVO vo = null;
        try {
        	if(UsitCodeConstants.TYPE_USER.equals(type)) {
        		vo = loginService.getLoginMember(id);
        	}else if(UsitCodeConstants.TYPE_SELLER.equals(type)) {
        		vo = loginService.getLoginShopMember(id);
        	}
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("@@@ selectLoginList  Exception=" + e.toString());
            throw new UsernameNotFoundException("로그인 정보 조회 중 오류가 발생하였습니다.");
        }

        if (vo == null) {
            Map<String, Object> logInfoMap = new HashMap<String, Object>();

//            logInfoMap.put("loginId", vo.getMemberEmail());
            logInfoMap.put("loginId", id);
            logInfoMap.put("loginPassword", pwd);

            logger.debug("@@@ request.RemoteAddr=" + request.getRemoteAddr());

            try {
                logInfoMap.put("loginContents", "로그인 아이디 없음");
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("@@@ loadUserDetails Exception=" + e.toString());
            }

//            throw new UsernameNotFoundException("등록된 사용자ID가 아닙니다. 다시한번 확인해 주세요.");
        }

        if( vo == null || vo.getMemberRoles().size() < 1) { // sets default as 'USIT_USER'
        	
        	if(vo == null) {
        	vo = new SessionVO();
        	vo.setMemberId(id);
        	vo.setMemberName(id);
        	}
            List<String> _roll = new ArrayList<String>();
            _roll.add("USIT_USER");
            vo.setMemberRoles(_roll);
            vo.setPassword("");
        }

        SignedMember signeduser = new SignedMember(vo);

        return signeduser;
   }

}
