package com.temitope.myblogapp.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogPostRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    @NotBlank(message = "Content is required")
    private String content;
    @Size(max = 500, message = "Summary must be less than 500 characters")
    private String summary;
}
