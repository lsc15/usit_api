package com.usit.app.spring.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.AES256Util;

public class BaseAuthenticationSuccessHandler
extends SimpleUrlAuthenticationSuccessHandler {

  private RequestCache requestCache = new HttpSessionRequestCache();
  private final String KEY="38.dbwlt.$$";

  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication)
    throws ServletException, IOException {

      SignedMember user = (SignedMember) authentication.getPrincipal();
      String loginPswd = (String) authentication.getCredentials();

      String enc = "";
      String token = user.getUserId() + ":" + user.getPassword();
      System.out.println("token:" + token);

      AES256Util aes256Util = new AES256Util("38.dbwlt.$$");

      try {
          enc = aes256Util.encrypt(token);
      } catch(Exception e) {

      }

//      String base64Credentials = new String(Base64.encodeBase64(token.getBytes()));

      Map<String, Object> resultMap = new HashMap<String, Object>();
      Map<String, String> tokenMap = new HashMap<String, String>();
     tokenMap.put("token", enc);
//      tokenMap.put("refreshToken", authentication.getToken());

     resultMap.put("result_code", "0000");
     resultMap.put("result_msg", "");
     resultMap.put("data", tokenMap);

     response.setStatus(HttpStatus.OK.value());
     response.setContentType(MediaType.APPLICATION_JSON_VALUE);
     new ObjectMapper().writeValue(response.getWriter(), resultMap);

      clearAuthenticationAttributes(request);
  }

  public String getKey() {
      return KEY;
  }

  public void setRequestCache(RequestCache requestCache) {
      this.requestCache = requestCache;
  }
}
