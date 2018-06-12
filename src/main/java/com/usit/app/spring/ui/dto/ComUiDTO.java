package com.usit.app.spring.ui.dto;

import java.io.Serializable;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ComUiDTO implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 2728772183566725884L;

    private Object oject;
    private JSONObject jsonObject;

    /**
     * <P>
     * Request Body를 JSON으로 반환한다.
     * </P>
     * @return
     * @author : Kim Ho Se
     */
    public JSONObject getRequestBodyToJSON() {
        return this.jsonObject;
    }

    /**
     * <P>
     * Request Body를 Object으로 반환한다.
     * </P>
     * @return
     * @author : Kim Ho Se
     */
    public Object getRequestBodyToObject() {
        return this.oject;
    }

    public void setRequestBodyToJSON(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setRequestBodyToObject(Object object) {
        this.oject = object;
    }

}
