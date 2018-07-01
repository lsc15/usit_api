package com.usit.app.spring.security.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.tools.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.FirebaseDatabase;
import com.usit.app.spring.security.authentication.SkipPathRequestMatcher;
import com.usit.app.spring.security.service.UserDetailsService;
import com.usit.app.spring.util.AES256Util;
import com.usit.app.spring.util.UsitCodeConstants;

public class TokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private UserDetailsService detailService;

    @Autowired
    public TokenAuthenticationProcessingFilter() {
        super("/**");
    }

    @Autowired
    public TokenAuthenticationProcessingFilter(UserDetailsService detailService, SkipPathRequestMatcher matcher) {
    	//SecurityConfig설정
//    	super(matcher);
        super("/**");
        this.detailService = detailService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);

            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Request is to process authentication");
        }

        Authentication authResult = null;

        try {
            	if(request.getHeader("usit-auth") != null) {
                authResult = attemptAuthentication(request, response);
                if (authResult == null) {
                    // return immediately as subclass has indicated that it hasn't completed
                    // authentication
                    return;
                }
            }else if(request.getHeader("usitshop-auth") != null) {
                authResult = attemptAuthenticationShop(request, response);
                if (authResult == null) {
                    // return immediately as subclass has indicated that it hasn't completed
                    // authentication
                    return;
                }
            }


//            sessionStrategy.onAuthentication(authResult, request, response);
        } catch (InternalAuthenticationServiceException failed) {
            logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication(request, response, failed);
            return;
        } catch (AuthenticationException failed) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, failed);
            return;
        }

        logger.debug("@@@@@@111.");
        //chain.doFilter(request, response);

        logger.debug("@@@@@@222.");
        successfulAuthentication(request, response, chain, authResult);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
    	request.getHeaderNames();
        String tokenPayload = request.getHeader("usit-auth");

//        AES256Util aes256Util = new AES256Util("38.dbwlt.$$");

        String token = "";
        String id = "";
        String password = "";
        String uid = "";
        String type = UsitCodeConstants.TYPE_USER;
        try {
//            token = aes256Util.decrypt(tokenPayload);
         // idToken comes from the client app (shown above)
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdTokenAsync(tokenPayload).get();
            uid = decodedToken.getUid();
            System.out.println("!~!~! uid : " + uid);
        } catch(Exception e) {

        	e.printStackTrace();
        	throw new AuthenticationServiceException("_INVALID_AUTH_TOKEN_");
        }
        System.out.println("token:" + token);

//        String[] tokens = token.split(Pattern.quote(":"));

//        if(tokens.length > 1) {
//
//            id = tokens[0];
//            password = tokens[1];
//
//        } else {
//            throw new AuthenticationServiceException("_INVALID_AUTH_TOKEN_");
//        }

        UserDetails user = detailService.getUserByUid(type,uid, password);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), password);

        setDetails(request, authRequest);

        return getAuthenticationManager().authenticate(authRequest);
    }
    
    
    
    
    
    
    
    public Authentication attemptAuthenticationShop(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
    	request.getHeaderNames();
        String tokenPayload = request.getHeader("usitshop-auth");

//        AES256Util aes256Util = new AES256Util("38.dbwlt.$$");

        String token = "";
        String id = "";
        String password = "";
        String uid = "";
        String type = UsitCodeConstants.TYPE_SELLER;
        try {
        	
        	// Retrieve my other app.
        	FirebaseApp app = FirebaseApp.getInstance("secondary");
        	// Get the database for the other app.
        	// FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
        	
            FirebaseToken decodedToken = FirebaseAuth.getInstance(app).verifyIdTokenAsync(tokenPayload).get();
            uid = decodedToken.getUid();
            System.out.println("!~!~! uid : " + uid);
        } catch(Exception e) {

        	e.printStackTrace();
        	throw new AuthenticationServiceException("_INVALID_AUTH_TOKEN_");
        }
        System.out.println("token:" + token);

        UserDetails user = detailService.getUserByUid(type,uid, password);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), password);

        setDetails(request, authRequest);

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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }


}
