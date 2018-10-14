package com.usit.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.util.StringUtils;
import com.usit.app.spring.exception.FrameworkException;
import com.usit.app.spring.ui.dto.ComUiDTO;
import com.usit.app.spring.web.CommonHeaderController;
import com.usit.domain.Content;
import com.usit.service.ContentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contents")
public class ContentController extends CommonHeaderController{


    @Autowired
    private ContentService contentService;

    @RequestMapping(value="/type/{type}", method=RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, @PathVariable String type, @RequestParam("useYn") String useYn,@RequestParam("curPage") int curPage, @RequestParam("perPage") int perPage) {
        ModelAndView mav = new ModelAndView("jsonView");

        String resultCode = "0000";
        String resultMsg = "";

        /*
컨텐트타입	601	스토리
컨텐트타입	602	뉴스
컨텐트타입	603	공지사항
컨텐트타입	604	이벤트
컨텐트타입	605	FAQ
         */

        logger.debug("type:{}", type);

        Content paramContent = new Content();
        paramContent.setContentTypeCd(type);

        logger.debug("paramContent.getContentTypeCd():{}", paramContent.getContentTypeCd());

        Pageable pageRequest = new PageRequest(curPage, perPage, Sort.Direction.DESC, "contentId");
        Page<Content> contentList = contentService.getList(paramContent.getContentTypeCd(),useYn, pageRequest);

//        resultData.put("content", contentList.getContent());
//        resultData.put("isFirst", contentList.isFirst());
//        resultData.put("isLast", contentList.isLast());
//        resultData.put("number", contentList.getNumber());
//        resultData.put("totalPages", contentList.getTotalPages());
//        resultData.put("totalCount", contentList.getTotalElements());

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", contentList);

        return mav;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, @PathVariable String id) {
        ModelAndView mav = new ModelAndView("jsonView");
        String resultCode = "0000";
        String resultMsg = "";
        Content content = new Content();
        try {

            content = contentService.getData(Integer.parseInt(id));


        }catch(Exception e) {
            resultCode = "-1";
            resultMsg = "존재하지 않는 정보입니다";
        }

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", content);

        return mav;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value="", method=RequestMethod.POST)
    public ModelAndView saveContent(ComUiDTO params, HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("jsonView");
        Map<String, String> paramData = (Map<String, String>)params.getRequestBodyToObject();
        String resultCode = "0000";
        String resultMsg = "";
        Content content = new Content();

        

        String contentTypeCd = paramData.get("contentTypeCd");
        String detail = paramData.get("detail");
        String title = paramData.get("title");
        String link = paramData.get("link");
        String img = paramData.get("img");
        String author = paramData.get("author");
        String displayDate = paramData.get("displayDate");
        String useYn = paramData.get("useYn");

        try {

            if("601".equals(contentTypeCd)
                    || "602".equals(contentTypeCd)
                    || "603".equals(contentTypeCd)
                    || "604".equals(contentTypeCd)
                    || "605".equals(contentTypeCd)
                    || "606".equals(contentTypeCd)) {

//                if(StringUtils.isNullOrEmpty(author)) {
//                    if(getSignedMember() == null) {
//                        throw new FrameworkException("-101", "작성자는 필수입니다");
//                    }
//                    author = getSignedMember().getMemberInfo().getMemberName();
//                }

                if(StringUtils.isNullOrEmpty(title)) {
                    throw new FrameworkException("-101", "제목은 필수입니다");
                }
                content.setContentTypeCd(contentTypeCd);
                content.setTitle(title);
                content.setDetail(detail);
                content.setLink(link);
                content.setImg(img);
                content.setAuthor(author);
                content.setDisplayDate(displayDate);
                content.setUseYn(useYn);
                content = contentService.putData(content);

            } else {
                throw new FrameworkException("-100", "게시글 분류를 알 수 없습니다");
            }



        } catch(FrameworkException fe) {
            resultCode = fe.getMsgKey();
            resultMsg = fe.getMsg();
        } catch(Exception e) {
            logger.error("{}", e);
            resultCode = "-1";
            resultMsg = "저장 중 오류가 발생하였습니다";
        }

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", content);

        return mav;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public ModelAndView modifyContent(ComUiDTO params, HttpServletRequest request, @PathVariable String id) {
        ModelAndView mav = new ModelAndView("jsonView");
        Map<String, String> paramData = (Map<String, String>)params.getRequestBodyToObject();

//        Map<String, Object> resultData = new HashMap<String, Object>();
        String resultCode = "0000";
        String resultMsg = "";
        Content content = new Content();

        String contentTypeCd = paramData.get("contentTypeCd");
        String detail = paramData.get("detail");
        String title = paramData.get("title");
        String link = paramData.get("link");
        String author = paramData.get("author");
        String displayDate = paramData.get("displayDate");
        String img = paramData.get("img");
        String useYn = paramData.get("useYn");

        try {
            content.setContentId(Integer.parseInt(id));
            content.setAuthor(author);
            content.setContentTypeCd(contentTypeCd);
            content.setAuthor(author);
            content.setTitle(title);
            content.setDetail(detail);
            content.setLink(link);
            content.setDisplayDate(displayDate);
            content.setImg(img);
            content.setUseYn(useYn);

            content = contentService.setData(content);


        }catch(Exception e) {
            resultCode = "-1";
            resultMsg = "저장 중 오류가 발생하였습니다";
        }

//        resultData.put("content", content);
        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
        mav.addObject("data", content);

        return mav;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ModelAndView deleteContent(HttpServletRequest request, @PathVariable String id) {
        ModelAndView mav = new ModelAndView("jsonView");

        String resultCode = "0000";
        String resultMsg = "";
        Content content = new Content();
        try {

            content.setContentId(Integer.parseInt(id));
            contentService.removeData(Integer.parseInt(id));

        }catch(Exception e) {
            resultCode = "-1";
            resultMsg = "삭제 중 오류가 발생하였습니다";
        }

        mav.addObject("result_code", resultCode);
        mav.addObject("result_msg", resultMsg);
//        mav.addObject("data", resultData);

        return mav;
    }


}
