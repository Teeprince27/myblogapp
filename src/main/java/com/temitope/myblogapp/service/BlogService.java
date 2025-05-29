package com.temitope.myblogapp.service;

import com.temitope.myblogapp.dto.request.BlogPostRequest;
import com.temitope.myblogapp.dto.response.BlogPostResponse;
import com.temitope.myblogapp.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface BlogService {
    PageResponse<BlogPostResponse> getAllPublicPosts(Pageable pageable);
    BlogPostResponse getPublicPostById(Long id);
    BlogPostResponse createPost(BlogPostRequest request, Authentication authentication);
    BlogPostResponse updatePost(Long id, BlogPostRequest request, Authentication authentication);
    String deletePost(Long id, Authentication authentication);


}
