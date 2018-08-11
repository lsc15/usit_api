package com.usit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.usit.app.spring.service.CommonHeaderService;
import com.usit.domain.Content;
import com.usit.repository.ContentRepository;
import com.usit.service.ContentService;

@Service
public class ContentServiceImpl extends CommonHeaderService implements ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Override
    public Page<Content> getList(String contentTypeCd,String useYn, Pageable pageable){

    		Page<Content> list;
    		if(!"all".equals(useYn)) {
    			list = contentRepository.findByUseYnAndContentTypeCd(useYn,contentTypeCd, pageable);
    		}else {
    			list = contentRepository.findByContentTypeCd(contentTypeCd, pageable);	
    		}
        
        
        
        logger.info("list.size():{}", list.getSize());
        logger.info("pageable.getPageNumber():{}", pageable.getPageNumber());
        logger.info("pageable.getPageSize():{}", pageable.getPageSize());

//        Page<Content> contents = contentRepository.findAll(pageable);
//        logger.info("contents.getContent().size():{}", contents.getContent().size());

        return list;

    }

    @Override
    public Content getData(int id){

        Content rsltContent = contentRepository.findOne(id);

        return rsltContent;

    }

    @Override
    public Content putData(Content content) {
        Content rsltContent = contentRepository.save(content);
        return rsltContent;
    }

    @Override
    public Content setData(Content content) {
    		Content updateContent = contentRepository.findOne(content.getContentId());
    		updateContent.setContentTypeCd(content.getContentTypeCd());
    		updateContent.setTitle(content.getTitle());
    		updateContent.setDetail(content.getDetail());
    		updateContent.setLink(content.getLink());
    		updateContent.setImg(content.getImg());
    		updateContent.setAuthor(content.getAuthor());
    		updateContent.setDisplayDate(content.getDisplayDate());
    		updateContent.setUseYn(content.getUseYn());
    		
    		return contentRepository.save(updateContent);
    }

    @Override
    public void removeData(int id) {
        contentRepository.delete(id);
    }

}
