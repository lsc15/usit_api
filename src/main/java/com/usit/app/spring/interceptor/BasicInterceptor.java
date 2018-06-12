package com.usit.app.spring.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class BasicInterceptor extends HandlerInterceptorAdapter{

    Logger logger = LoggerFactory.getLogger(BasicInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.debug("@@@");
        logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@ BasicInterceptor.preHandle");
        logger.debug("@@@");

        String reqPath = request.getServletPath();
        String path[] = reqPath.split("/");

        //TODO

        return true;


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        logger.debug("@@@");
        logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@ BasicInterceptor.postHandle");
        logger.debug("@@@");

        String reqPath = request.getServletPath();
        String path[] = reqPath.split("/");

        //TODO

    }
}
