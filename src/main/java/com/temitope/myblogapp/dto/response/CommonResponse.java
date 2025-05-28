package com.temitope.myblogapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommonResponse {
    private Object data;

    public static CommonResponse error(String message) {
        return CommonResponse.builder()
                .data(Map.of("error", message))
                .build();
    }

    public static CommonResponse success(Object payload) {
        return CommonResponse.builder()
                .data(payload)
                .build();
    }
}
