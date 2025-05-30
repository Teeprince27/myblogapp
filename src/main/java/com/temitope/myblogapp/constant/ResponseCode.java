package com.temitope.myblogapp.constant;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("200", "Successful"),
    USER_NAME_TAKEN("403", "Username is already taken!"),
    EMAIL_TAKEN("403", "Email is already in use!"),
    USER_NOT_FOUND("404", "User not found"),
    INCORECT_USER("404", "Wrong user details, please check and try again"),
    INVALID_DECISION("405", "Invalid decision. Use APPROVE or REJECT"),
    BLOG_POST_NOT_FOUND("404", "Blog post not found"),
    USER_CANNOT_UPDATE_POST("409", "You don't have permission to update this post"),
    USER_CANNOT_DELETE_POST("409", "You don't have permission to delete this post"),
    BLOG_POST_ALREADY_APPROVED("404", "Post is not pending approval");


    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return code + " - " + description;
    }
}
