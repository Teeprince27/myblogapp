package com.temitope.myblogapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApprovalRequest {
    @NotBlank(message = "Decision is required")
    private String decision;
    private String rejectionReason;
}
