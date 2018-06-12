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

public class TokenAuthenticationSuccessHandler
extends SimpleUrlAuthenticationSuccessHandler {

  private RequestCache requestCache = new HttpSessionRequestCache();

  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication)
    throws ServletException, IOException {

      clearAuthenticationAttributes(request);
  }

  public void setRequestCache(RequestCache requestCache) {
      this.requestCache = requestCache;
  }
}
