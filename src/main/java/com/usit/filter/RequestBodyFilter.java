package com.usit.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.util.HttpRequestWrapper;

@Service("RequestBodyFilter")
public class RequestBodyFilter implements Filter {

    Logger globalLogger = LoggerFactory.getLogger(RequestBodyFilter.class);

    private final String REMOTE_HOST_MDC_KEY = "remote_host";
    private final String CURRENT_DATE = "current_date";
    private final String REQUEST_METHOD_MDC_KEY = "request_method";
    private final String REQUEST_BEHAVIOR_MDC_KEY = "request_behavior";
    private final String REQUEST_EXT_MDC_KEY = "request_ext";
    private final String USER_ID_KEY = "cust_no";

    private FilterConfig fc;

    @Override
    public void destroy() {
        this.fc = null;

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        globalLogger.debug("#### RequestBodyFilter ####");

        StringBuilder strBuilder = new StringBuilder();
        BufferedReader br = null;
        ServletInputStream is = null;

        String scheme = "";

        HttpServletRequest sRequest = (HttpServletRequest) request;
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(sRequest);

//        Enumeration<String> requestHeaderNames = requestWrapper.getHeaderNames();
//        while (requestHeaderNames.hasMoreElements()) {
//            String headerName = requestHeaderNames.nextElement();
//            Enumeration<String> headers = requestWrapper.getHeaders(headerName);
//            while (headers.hasMoreElements()) {
//                String headerValue = headers.nextElement();
//                globalLogger.debug(" Request HEADER - {} : {} ", headerName, headerValue );
//            }
//
//        }

        scheme = sRequest.getScheme();

        globalLogger.debug("request.getScheme:{}", scheme);
        globalLogger.debug("request.getServletPath():{}", sRequest.getServletPath());
        globalLogger.debug("request.isSecure():{}", sRequest.isSecure());

        try{

            is = requestWrapper.getInputStream();

            globalLogger.debug("request.getCharacterEncoding():{}", sRequest.getCharacterEncoding());

            if(is != null){
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                char[] charBuffer = new char[128];
                int bytesRead = -01;
                while((bytesRead = br.read(charBuffer)) > 0){
                    strBuilder.append(charBuffer, 0, bytesRead);
                }
            }

        }catch(IOException e){
            globalLogger.error("error occured while getting request stream : \n{}", e);
        }finally{
            if(br != null){
                try{
                    br.close();
                }catch(Exception e){

                }
            }
            if(is != null){
                try{
                    is.close();
                }catch(Exception e){

                }
            }
        }

        try{

            globalLogger.debug("strBuilder.toString():" + strBuilder.toString());
            if(strBuilder.toString().startsWith("{")){
            	Object object = new ObjectMapper().readValue(strBuilder.toString().getBytes(), Object.class);
//                JSONObject jo = (JSONObject)JSONValue.parseWithException(strBuilder.toString());
            	requestWrapper.setAttribute("requestInfo", object);
            }


        }catch(Exception e){
            globalLogger.error("Execption", e);
        }

        try {
            chain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            globalLogger.error("Execption", e);
            throw new FrameworkException(e, "ERR");
        }

    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
        this.fc = fc;
    }


}
