package com.temitope.myblogapp.service.impl;


import com.temitope.myblogapp.constant.ResponseCode;
import com.temitope.myblogapp.dto.request.AuthRequest;
import com.temitope.myblogapp.dto.request.RegisterRequest;
import com.temitope.myblogapp.dto.response.AuthResponse;
import com.temitope.myblogapp.dto.response.UserResponse;
import com.temitope.myblogapp.model.User;
import com.temitope.myblogapp.repository.UserRepository;
import com.temitope.myblogapp.security.JwtTokenProvider;
import com.temitope.myblogapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;


    public AuthResponse login(AuthRequest request) {
        log.info("Login request: {}", request);
        String token;
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(ResponseCode.USER_NOT_FOUND.getDescription()));
        if(user!=null && user.getPassword().equals(passwordEncoder.encode(request.getPassword()))) {
             token = tokenProvider.generateToken(user);
        }else{
            throw new RuntimeException(ResponseCode.USER_NOT_FOUND.getDescription());
        }



        return new AuthResponse(token, "Bearer", mapToUserResponse(user));
    }


    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUserName())) {
            throw new RuntimeException(ResponseCode.USER_NAME_TAKEN.getDescription());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(ResponseCode.EMAIL_TAKEN.getDescription());
        }

        User user = new User();
        user.setUsername(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateToken(savedUser);

        return new AuthResponse(token, "Bearer", mapToUserResponse(savedUser));
    }



    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setRole(user.getRole());
        return userResponse;

    }
}
