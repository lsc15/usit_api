package com.usit.app.spring.security.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private Logger logger = LoggerFactory.getLogger(BaseAuthenticationFailureHandler.class);
	private final Map<String, String> failureUrlMap = new HashMap<String, String>();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		logger.debug("##### BaseAuthenticationFailureHandler.onAuthenticationFailure #####");
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> resultMap = new HashMap<String, Object>();

	    resultMap.put("result_code", "-1000");
	    resultMap.put("result_msg", "");
	    resultMap.put("data", "로그인 실패");

	    response.setStatus(HttpStatus.OK.value());
	    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

	    String jsonString = om.writeValueAsString(resultMap);
	    OutputStream out = response.getOutputStream();
        out.write(jsonString.getBytes());

	}

}
