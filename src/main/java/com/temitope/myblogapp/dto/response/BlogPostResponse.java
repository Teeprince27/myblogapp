package com.temitope.myblogapp.dto.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.temitope.myblogapp.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogPostResponse {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private PostStatus status;
    private UserResponse author;
    private UserResponse approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String rejectionReason;
}
