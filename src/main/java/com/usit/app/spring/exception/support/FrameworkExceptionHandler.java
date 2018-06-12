package com.usit.app.spring.exception.support;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.exception.FrameworkExceptionModel;

@Controller
@ControllerAdvice
public class FrameworkExceptionHandler implements HandlerExceptionResolver, Ordered {
    protected final Logger logger = LoggerFactory.getLogger(FrameworkExceptionHandler.class);
    private int order;

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object obj, Exception ex) {
        FrameworkExceptionModel exModel = new FrameworkExceptionModel();

        List<StackTraceElement> list = new ArrayList<StackTraceElement>();
        for (int i = 0; i < ex.getStackTrace().length; i++) {
            list.add(ex.getStackTrace()[i]);
        }
        exModel.setStackTraceList(list);

        String reqUrl = req.getServletPath();
        this.logger.debug("reqUrl:" + reqUrl);
        this.logger.error("[EXCEPTION OCURRED]", ex);

        String nextView = null;

        if ((ex instanceof FrameworkException)) {
            FrameworkException fe = (FrameworkException) ex;

            exModel.setMessage(fe.getMsg());
            exModel.setMsgKey(fe.getMsgKey());
            exModel.setMsgArg(fe.getMsgArg());

            if ((fe.getNextView() != null) && (!fe.getNextView().equals(""))) {
                nextView = fe.getNextView();
            }
        } else {
            exModel.setMsgKey("-9999");
            exModel.setMessage("Exception Occured at " + ex.getStackTrace()[0].getClassName());
        }

        ModelAndView mv = new ModelAndView("jsonView");

        mv.addObject("result_code", exModel.getMsgKey());
        mv.addObject("result_msg", exModel.getMessage());
        mv.addObject("data", new JSONObject());


        return mv;
    }

}
