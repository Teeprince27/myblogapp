package com.temitope.myblogapp.controller;

import com.temitope.myblogapp.dto.request.BlogPostRequest;
import com.temitope.myblogapp.dto.response.BlogPostResponse;
import com.temitope.myblogapp.dto.response.CommonResponse;
import com.temitope.myblogapp.dto.response.PageResponse;
import com.temitope.myblogapp.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/blogs")
@Tag(name = "Blog Management", description = "Blog content management endpoints")
public class BlogController {


    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/get-all-post")
    @Operation(summary = "Get all published blog posts", description = "Retrieve all published blog posts with pagination")
    public ResponseEntity<PageResponse<BlogPostResponse>> getAllPublishedBlogs(Pageable pageable) {
        PageResponse<BlogPostResponse> blogs = blogService.getAllPublicPosts(pageable);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get blog post by ID", description = "Retrieve a specific published blog post")
    public ResponseEntity<BlogPostResponse> getBlogById(@PathVariable Long id) {
        BlogPostResponse blog = blogService.getPublicPostById(id);
        return ResponseEntity.ok(blog);
    }

    @PostMapping("/create-post")
    @Operation(summary = "Create a new blog post", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('EXTERNAL_USER') or hasRole('ADMIN')")
    public ResponseEntity<BlogPostResponse> createPost(
            @Valid @RequestBody BlogPostRequest request,
            Authentication authentication) {
        log.info("Create a new blog post");
        BlogPostResponse blog = blogService.createPost(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(blog);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a blog post", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('EXTERNAL_USER') or hasRole('ADMIN')")
    public ResponseEntity<BlogPostResponse> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody BlogPostRequest request,
            Authentication authentication) {
        BlogPostResponse blog = blogService.updatePost(id, request, authentication);
        return ResponseEntity.ok(blog);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a blog post", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('EXTERNAL_USER') or hasRole('ADMIN')")
    public ResponseEntity<CommonResponse> deleteBlog(
            @PathVariable Long id,
            Authentication authentication) {
        String message = blogService.deletePost(id, authentication);
        return ResponseEntity.ok(CommonResponse.success(Map.of("message", message)));
    }


}


