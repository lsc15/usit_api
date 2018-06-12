package com.usit.app.spring.exception;

import java.util.List;


public class FrameworkExceptionModel {

    private String message;
    private Object msgArg;
    private String msgKey;
    private List<StackTraceElement> stackTraceList;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgKey() {
        return this.msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public Object getMsgArg() {
        return this.msgArg;
    }

    public void setMsgArg(Object msgArg) {
        this.msgArg = msgArg;
    }

    public List<StackTraceElement> getStackTraceList() {
        return this.stackTraceList;
    }

    public void setStackTraceList(List<StackTraceElement> stackTraceList) {
        this.stackTraceList = stackTraceList;
    }

}
