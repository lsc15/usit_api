package com.usit.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;


//도메인필터
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

//        if(HttpMethod.OPTIONS.name().equals(request.getMethod())) {

            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Origin", "http://usit.co.kr");'
//        response.setHeader("Access-Control-Allow-Origin", "http://192.168.1.182:3000");
//            response.addHeader("Access-Control-Allow-Origin", "http://192.168.1.182:3000");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "X-Auth-Token, usit-auth,usitshop-auth, X-Requested-With, Content-Type");
//            response.setHeader("Access-Control-Allow-Headers", "X-Auth-Token, usitshop-auth, Content-Type");
//        } else {
//        	chain.doFilter(req, res);
//        }
            
            
            
            if (request.getMethod().equals("OPTIONS")) {

                System.out.println("OPTIONS >> ");
                try {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().print("OK");
                    response.getWriter().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                chain.doFilter(request, response);
            }
            
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}
}
