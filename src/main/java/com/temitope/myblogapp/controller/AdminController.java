package com.temitope.myblogapp.controller;


import com.temitope.myblogapp.dto.request.ApprovalRequest;
import com.temitope.myblogapp.dto.response.ApiResponse;
import com.temitope.myblogapp.dto.response.BlogPostResponse;
import com.temitope.myblogapp.dto.response.PageResponse;
import com.temitope.myblogapp.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin management endpoints")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/posts/pending")
    @Operation(summary = "Get pending blogs", description = "Retrieve all blog posts pending approval")
    public ResponseEntity<PageResponse<BlogPostResponse>> getPendingPosts(Pageable pageable) {
        PageResponse<BlogPostResponse> blogs = adminService.getPendingPosts(pageable);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/posts/delete/pending")
    @Operation(summary = "Get pending blogs", description = "Retrieve all blog posts pending approval")
    public ResponseEntity<PageResponse<BlogPostResponse>> getDeletePendingPosts(Pageable pageable) {
        PageResponse<BlogPostResponse> blogs = adminService.getDeletePendingPosts(pageable);
        return ResponseEntity.ok(blogs);
    }

    @PutMapping("/posts/{id}/approve")
    @Operation(summary = "Approve blog post", description = "Approve a pending blog post")
    public ResponseEntity<BlogPostResponse> approveBlog(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalRequest request,
            Authentication authentication) {
        BlogPostResponse blog = adminService.approvePost(id, request, authentication);
        return ResponseEntity.ok(blog);
    }

    @GetMapping("/posts")
    @Operation(summary = "Get all blog posts (admin view)")
    public ResponseEntity<ApiResponse<PageResponse<BlogPostResponse>>> getAllPosts(
            Pageable pageable) {

        PageResponse<BlogPostResponse> response = adminService.getAllPosts(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }




}
