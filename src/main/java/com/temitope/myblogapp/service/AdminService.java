package com.temitope.myblogapp.service;

import com.temitope.myblogapp.dto.request.ApprovalRequest;
import com.temitope.myblogapp.dto.response.BlogPostResponse;
import com.temitope.myblogapp.dto.response.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Pageable;


public interface AdminService {
    PageResponse<BlogPostResponse> getPendingPosts(Pageable pageable);
    PageResponse<BlogPostResponse> getDeletePendingPosts(Pageable pageable);
    BlogPostResponse approvePost(Long postId, ApprovalRequest request, Authentication authentication);
}
