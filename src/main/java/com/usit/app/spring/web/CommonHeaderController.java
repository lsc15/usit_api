package com.usit.app.spring.web;

import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.usit.app.spring.security.domain.SignedMember;
import com.usit.app.spring.util.SessionVO;

public class CommonHeaderController extends AbstractController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String LAST_MODIFIED_METHOD_SUFFIX = "LastModified";
    public static final String DEFAULT_COMMAND_NAME = "command";
    private Object delegate;
    protected final Map<String, Method> handlerMethodMap = new HashMap<String, Method>();

    protected final Map<String, Method> lastModifiedMethodMap = new HashMap<String, Method>();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected final Map<Class, Method> exceptionHandlerMap = new HashMap();

    private WebBindingInitializer webBindingInitializer;

    @SuppressWarnings({ "rawtypes", "unused" })
    private Map<Class, PropertyEditor> customEditors;

    public CommonHeaderController() {
        this.delegate = this;
        registerHandlerMethods(this.delegate);
    }

    private void registerHandlerMethods(Object delegate) {
        this.handlerMethodMap.clear();
        this.lastModifiedMethodMap.clear();
        this.exceptionHandlerMap.clear();

        Method[] methods = delegate.getClass().getMethods();
        for (Method method : methods) {
            if (isExceptionHandlerMethod(method)) {
                registerExceptionHandlerMethod(method);
            } else if (isHandlerMethod(method)) {
                registerHandlerMethod(method);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean isHandlerMethod(Method method) {
        Class<?> returnType = method.getReturnType();
        String targetClassName = method.getDeclaringClass().getName();

        if ((targetClassName.startsWith("java.")) || (targetClassName.startsWith("org.springframework.")) ||
                (targetClassName.startsWith("com.usit")) ||
                method.getDeclaringClass() == CommonHeaderController.class) {

            return false;

        }
        if ((ModelAndView.class.equals(returnType)) || (Map.class.equals(returnType)) || (String.class.equals(returnType))
                || (Void.TYPE.equals(returnType))) {

            Class[] parameterTypes = method.getParameterTypes();
            if (("handleRequest".equals(method.getName())) && (parameterTypes.length == 2)) {
                return false;
            }
            return (!"initBinder".equals(method.getName())) || (parameterTypes.length != 2);
        }

        return false;
    }

    @SuppressWarnings("rawtypes")
    private boolean isExceptionHandlerMethod(Method method) {
        if (isHandlerMethod(method)) {
            Class[] paramTypes = method.getParameterTypes();
            for (Class paramType : paramTypes) {
                if (Throwable.class.isAssignableFrom(paramType)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void registerHandlerMethod(Method method) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Found action method [" + method + "]");
        }

        String methodName = method.getName();

        if (this.handlerMethodMap.containsKey(methodName)) {
            throw new IllegalStateException("컨트롤러 내 메소드 오버로딩을 지원하지 않습니다. "
                    + method.getDeclaringClass().getName() + "."
                    + methodName + "()");
        }

        this.handlerMethodMap.put(methodName, method);
    }

    @SuppressWarnings("rawtypes")
    private void registerExceptionHandlerMethod(Method method) {
        Class[] paramTypes = method.getParameterTypes();
        for (Class paramType : paramTypes) {
            if (Throwable.class.isAssignableFrom(paramType)) {
                this.exceptionHandlerMap.put(paramType, method);
            }
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Found exception handler method [" + method + "]");
        }
    }

    @SuppressWarnings("unused")
    private void registerLastModifiedMethodIfExists(Object delegate, Method method) {
        try {
            Method lastModifiedMethod = delegate.getClass().getMethod(method.getName() + "LastModified", new Class[] { HttpServletRequest.class });

            Class<?> returnType = lastModifiedMethod.getReturnType();
            if ((!Long.TYPE.equals(returnType)) && (!Long.class.equals(returnType))) {
                throw new IllegalStateException("last-modified method ["
                        + lastModifiedMethod
                        + "] declares an invalid return type - needs to be 'long' or 'Long'");
            }

            this.lastModifiedMethodMap.put(method.getName(), lastModifiedMethod);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Found last-modified method for handler method [" + method + "]");
            }
        } catch (NoSuchMethodException ex) {
            logger.debug("registerLastModifiedMethodIfExists  NoSuchMethodException=" + ex.toString());
        }
    }

    public SignedMember getSignedMember() {
        if ((SecurityContextHolder.getContext() == null) || (SecurityContextHolder.getContext().getAuthentication() == null)) {
            return null;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if ((principal instanceof UserDetails)) {
            return (SignedMember) principal;
        }

        return null;

    }

    public SessionVO getUserInfo() {
    	SignedMember sigendUser = getSignedMember();
        if(sigendUser != null){
            return sigendUser.getMemberInfo();
        }
        return null;
    }

    public String getMessage(String messageId) {
        return getMessageSourceAccessor().getMessage(messageId);
    }

    public String getMessage(String messageId, Locale locale) {
        return getMessageSourceAccessor().getMessage(messageId, locale);
    }

    public String getMessage(String messageId, Object[] params) {
        return getMessageSourceAccessor().getMessage(messageId, params);
    }

    public String getMessage(String messageId, Object[] params, Locale locale) {
        return getMessageSourceAccessor().getMessage(messageId, params, locale);
    }

    public String getMessage(String messageId, String defaultMessage) {
        return getMessageSourceAccessor().getMessage(messageId, defaultMessage);
    }

    public String getMessage(String messageId, String defaultMessage, Locale locale) {
        return getMessageSourceAccessor().getMessage(messageId, defaultMessage, locale);
    }

    public String getMessage(String messageId, Object[] params, String defaultMessage) {
        return getMessageSourceAccessor().getMessage(messageId, params, defaultMessage);
    }

    public String getMessage(String messageId, Object[] params, String defaultMessage, Locale locale) {
        return getMessageSourceAccessor().getMessage(messageId, params, defaultMessage, locale);
    }

    @Override
    protected final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }

}

