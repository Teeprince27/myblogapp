package com.temitope.myblogapp.service;

import com.temitope.myblogapp.dto.request.AuthRequest;
import com.temitope.myblogapp.dto.request.RegisterRequest;
import com.temitope.myblogapp.dto.response.AuthResponse;


public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}
