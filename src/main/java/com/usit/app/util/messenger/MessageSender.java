package com.usit.app.util.messenger;

import java.util.Map;

public interface MessageSender {

    public void send(Map<String, Object> params) throws Exception;
}
