package com.usit.app.spring.ui.adaptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ComWebArgumentsResolver implements HandlerMethodArgumentResolver{

    public static final Object UNRESOLVED = new Object();

    private UiAdaptor uiA;

    public void setUiAdaptor(UiAdaptor uiA) {
        this.uiA = uiA;
    }

    @Override
    public boolean supportsParameter(MethodParameter paramMethodParameter) {
        return true;

    }

    @Override
    public Object resolveArgument(MethodParameter paramMethodParameter,
            ModelAndViewContainer paramModelAndViewContainer, NativeWebRequest paramNativeWebRequest,
            WebDataBinderFactory paramWebDataBinderFactory) throws Exception {
        Class<?> type = paramMethodParameter.getParameterType();
        Object uiObject = null;

        if (this.uiA == null)
            return UNRESOLVED;

        if (type.equals(this.uiA.getModelName())) {
            HttpServletRequest request = (HttpServletRequest) paramNativeWebRequest.getNativeRequest();
            uiObject = this.uiA.convert(request);
            return uiObject;
        }

        return UNRESOLVED;
    }

}
