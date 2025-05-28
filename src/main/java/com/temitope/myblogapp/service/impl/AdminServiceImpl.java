package com.temitope.myblogapp.service.impl;

import com.temitope.myblogapp.constant.ResponseCode;
import com.temitope.myblogapp.dto.request.ApprovalRequest;
import com.temitope.myblogapp.dto.response.BlogPostResponse;
import com.temitope.myblogapp.dto.response.PageResponse;
import com.temitope.myblogapp.dto.response.UserResponse;
import com.temitope.myblogapp.enums.PostStatus;
import com.temitope.myblogapp.enums.UserRole;
import com.temitope.myblogapp.model.BlogPost;
import com.temitope.myblogapp.model.User;
import com.temitope.myblogapp.model.UserPrincipal;
import com.temitope.myblogapp.repository.BlogPostRepository;
import com.temitope.myblogapp.repository.UserRepository;
import com.temitope.myblogapp.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    public PageResponse<BlogPostResponse> getPendingPosts(Pageable pageable) {
        Page<BlogPost> posts = blogPostRepository.findPendingPostsByAuthorRole(
                PostStatus.PENDING_APPROVAL, UserRole.EXTERNAL_USER, pageable);
        return mapToPageResponse(posts);
    }

    @Transactional
    public BlogPostResponse approvePost(Long postId, ApprovalRequest request, Authentication authentication) {
        User admin = getCurrentUser(authentication);
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException(ResponseCode.BLOG_POST_NOT_FOUND.getDescription()));

        if (post.getStatus() != PostStatus.PENDING_APPROVAL) {
            throw new RuntimeException(ResponseCode.BLOG_POST_ALREADY_APPROVED.getDescription());
        }

        if ("APPROVE".equalsIgnoreCase(request.getDecision())) {
            post.setStatus(PostStatus.APPROVED);
            post.setApprovedBy(admin);
            post.setApprovedAt(LocalDateTime.now());
            post.setPublishedAt(LocalDateTime.now());
            post.setRejectionReason(null);
        } else if ("REJECT".equalsIgnoreCase(request.getDecision())) {
            post.setStatus(PostStatus.REJECTED);
            post.setRejectionReason(request.getRejectionReason());
            post.setApprovedBy(null);
            post.setApprovedAt(null);
            post.setPublishedAt(null);
        } else {
            throw new RuntimeException(ResponseCode.INVALID_DECISION.getDescription());
        }

        BlogPost updatedPost = blogPostRepository.save(post);
        return mapToResponse(updatedPost);
    }

    public PageResponse<BlogPostResponse> getAllPosts(Pageable pageable) {
        Page<BlogPost> posts = blogPostRepository.findAll(pageable);

        return mapToPageResponse(posts);
    }

    private User getCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private PageResponse<BlogPostResponse> mapToPageResponse(Page<BlogPost> page) {
        List<BlogPostResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    private BlogPostResponse mapToResponse(BlogPost post) {
        return new BlogPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getStatus(),
                mapToUserResponse(post.getAuthor()),
                post.getApprovedBy() != null ? mapToUserResponse(post.getApprovedBy()) : null,
                post.getApprovedAt(),
                post.getPublishedAt(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getRejectionReason()
        );
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
