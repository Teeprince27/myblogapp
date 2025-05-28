package com.temitope.myblogapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.temitope.myblogapp.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogPostSummaryResponse {
    private Long id;
    private String title;
    private String summary;
    private PostStatus status;
    private UserResponse author;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
}
