package com.usit.service;

import java.util.List;


import com.usit.domain.PostingHistory;

public interface PostingHistoryService {

	List<PostingHistory> getPostingHistoryList(String memberId);

	PostingHistory createPostingHistory(PostingHistory postingHistory);

	PostingHistory updatePostingHistory(PostingHistory postingHistory,int postingHistoryId,String memberId);

	void deletePostingHistory(int postingHistoryId);


}