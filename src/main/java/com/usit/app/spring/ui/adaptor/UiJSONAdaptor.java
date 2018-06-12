package com.usit.app.spring.ui.adaptor;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.usit.app.spring.ui.dto.ComUiDTO;

public class UiJSONAdaptor implements UiAdaptor{

    Logger logger = LoggerFactory.getLogger(UiJSONAdaptor.class);

    @Override
    public Object convert(HttpServletRequest request) throws Exception {
        ComUiDTO udto = new ComUiDTO();

        try{
        	Object obj = new Object();
//            JSONObject jo = new JSONObject();
            logger.debug("##REQUEST.ATTR:" + request.getAttribute("requestInfo"));
            if(request.getAttribute("requestInfo") != null){
//            	jo = (JSONObject)request.getAttribute("requestInfo");
            	obj = request.getAttribute("requestInfo");
            }
            udto.setRequestBodyToObject(obj);

        }catch(Exception e){
            logger.error("UiJSONAdaptor.error occured", e);
        }

        //request.setAttribute("ComUiDTO", udto);
        return udto;

    }

    @Override
    public Class<?> getModelName() {
        // TODO Auto-generated method stub
        return new ComUiDTO().getClass();
    }


}
