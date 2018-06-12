package com.usit.app.spring.ui.adaptor;

import javax.servlet.http.HttpServletRequest;

public abstract interface UiAdaptor {
    public abstract Object convert(HttpServletRequest paramHttpServletRequest) throws Exception;
    public abstract Class<?> getModelName();
}
