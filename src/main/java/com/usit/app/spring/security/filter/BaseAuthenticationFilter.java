package com.usit.app.spring.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usit.app.spring.security.domain.LoginRequest;
import com.usit.app.spring.security.service.UserDetailsService;
import com.usit.app.spring.util.UsitCodeConstants;
import com.usit.app.spring.util.WebUtil;

public class BaseAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


	static Logger logger = LoggerFactory.getLogger(BaseAuthenticationFilter.class);

	private UserDetailsService detailService;


    public BaseAuthenticationFilter()  {
        super("/users/sign-in");
    }

    @Autowired
    public BaseAuthenticationFilter(UserDetailsService detailService) {
    	super("/users/sign-in");
    	this.detailService = detailService;
    }


   /**
     * <pre>
     *
     * </pre>
     * @param N/A
     * @return N/A
     */
   @Override
   public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
	   
	   
	   
	   if(HttpMethod.OPTIONS.name().equals(request.getMethod())) {
	          response.setStatus(HttpServletResponse.SC_OK);
	          PrintWriter writer = response.getWriter();
	           writer.println("OK");

	      }
	   logger.debug("WebUtil.isAjax(request):{}", WebUtil.isAjax(request));
//	   if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
	   if (!HttpMethod.POST.name().equals(request.getMethod())) {
           throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
       }

       logger.debug("@@BaseAuthenticationFilter.attemptAuthentication@@@");

       LoginRequest loginRequest = new ObjectMapper().readValue(request.getReader(), LoginRequest.class);

       if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
           throw new AuthenticationServiceException("Username or Password not provided");
       }

       UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

       setDetails(request, authRequest);
       logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#############################");
       UserDetails user;
    	   user = detailService.loadUserDetails(authRequest);

       
       List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
       logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#############################22");

       return getAuthenticationManager().authenticate(authRequest);

   }


   /**
     * <pre>
     * setDetails
     * </pre>
     * @param HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest
     * @return N/A
     */
   protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest)
   {
       authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
   }


}
