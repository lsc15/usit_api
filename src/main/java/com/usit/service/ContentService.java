package com.usit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usit.domain.Content;

public interface ContentService {

	public Page<Content> getList(String type,String useYn, Pageable pageable);
	public Content getData(int id);
	public Content putData(Content content);
	public Content setData(Content content);
	public void removeData(int id);
}
