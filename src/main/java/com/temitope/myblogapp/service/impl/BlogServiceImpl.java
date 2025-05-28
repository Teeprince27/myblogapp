package com.temitope.myblogapp.service.impl;


import com.temitope.myblogapp.constant.ResponseCode;
import com.temitope.myblogapp.dto.request.BlogPostRequest;
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
import com.temitope.myblogapp.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Override
    public PageResponse<BlogPostResponse> getAllPublicPosts(Pageable pageable) {
        List<PostStatus> publicStatuses = Arrays.asList(PostStatus.PUBLISHED, PostStatus.APPROVED);
        Page<BlogPost> posts = blogPostRepository.findByStatusInOrderByPublishedAtDesc(publicStatuses, pageable);
        return mapToPageResponse(posts, this::mapToSummaryResponse);
    }

    @Override
    public BlogPostResponse getPublicPostById(Long id) {
        log.info("getPublicPostById{}", id);
        List<PostStatus> publicStatuses = Arrays.asList(PostStatus.PUBLISHED, PostStatus.APPROVED);
        BlogPost post = blogPostRepository.findByIdAndStatusIn(id, publicStatuses)
                .orElseThrow(() -> new RuntimeException("Blog post not found or not available"));
        return mapToResponse(post);
    }

    @Transactional
    @Override
    public BlogPostResponse createPost(BlogPostRequest request, Authentication authentication) {
        User author = getCurrentUser(authentication);

        BlogPost post = new BlogPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setAuthor(author);

        // Set status based on user role
        if (author.getRole() == UserRole.EMPLOYEE || author.getRole() == UserRole.ADMIN) {
            post.setStatus(PostStatus.PUBLISHED);
        } else {
            post.setStatus(PostStatus.PENDING_APPROVAL);
        }

        BlogPost savedPost = blogPostRepository.save(post);
        return mapToResponse(savedPost);
    }

    @Transactional
    @Override
    public BlogPostResponse updatePost(Long id, BlogPostRequest request, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ResponseCode.BLOG_POST_NOT_FOUND.getDescription()));
        UserRole role = currentUser.getRole();
        boolean isAuthor = post.getAuthor().getId().equals(currentUser.getId());
        if (role == UserRole.ADMIN || role == UserRole.EMPLOYEE) {
            applyUpdate(post, request);
        } else if (role == UserRole.EXTERNAL_USER) {
            if (!isAuthor) {
                throw new RuntimeException(ResponseCode.USER_CANNOT_UPDATE_POST.getDescription());
            }
            applyUpdate(post, request);
            post.setStatus(PostStatus.PENDING_APPROVAL);
            post.setApprovedBy(null);
            post.setApprovedAt(null);
            post.setPublishedAt(null);
        }
        else {
            throw new RuntimeException(ResponseCode.USER_CANNOT_UPDATE_POST.getDescription());
        }
        BlogPost updatedPost = blogPostRepository.save(post);
        return mapToResponse(updatedPost);
    }

    private void applyUpdate(BlogPost post, BlogPostRequest request) {
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
    }

    @Transactional
    @Override
    public String deletePost(Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ResponseCode.BLOG_POST_NOT_FOUND.getDescription()));

        UserRole role = currentUser.getRole();
        boolean isAuthor = post.getAuthor().getId().equals(currentUser.getId());

        if (role == UserRole.ADMIN || role == UserRole.EMPLOYEE) {
            blogPostRepository.delete(post);
            return "Post deleted successfully.";
        }

        if (role == UserRole.EXTERNAL_USER && isAuthor) {
            log.info("Marking Post for Deletion by user{}", role);
            post.setStatus(PostStatus.PENDING_DELETION);
            post.setApprovedBy(null);
            post.setApprovedAt(null);
            post.setPublishedAt(null);
            blogPostRepository.save(post);
            return "Your deletion request has been submitted for admin approval.";
        }

        throw new RuntimeException(ResponseCode.USER_CANNOT_DELETE_POST.getDescription());
    }


    @Override
    public PageResponse<BlogPostResponse> getUserPosts(Authentication authentication, Pageable pageable) {
        User user = getCurrentUser(authentication);
        Page<BlogPost> posts = blogPostRepository.findByAuthorOrderByCreatedAtDesc(user, pageable);
        return mapToPageResponse(posts, this::mapToResponse);
    }


    private User getCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
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

    private BlogPostResponse mapToSummaryResponse(BlogPost post) {

        BlogPostResponse response = new BlogPostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setSummary(post.getSummary());
        response.setStatus(post.getStatus());
        response.setAuthor(mapToUserResponse(post.getAuthor()));
        response.setApprovedAt(post.getApprovedAt());
        response.setPublishedAt(post.getPublishedAt());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setRejectionReason(post.getRejectionReason());
        response.setSummary(post.getSummary());
        response.setContent(post.getContent());
        return response;
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

    private <T> PageResponse<T> mapToPageResponse(Page<BlogPost> page, java.util.function.Function<BlogPost, T> mapper) {
        List<T> content = page.getContent().stream()
                .map(mapper)
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
}
