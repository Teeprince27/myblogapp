package com.temitope.myblogapp;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.temitope.myblogapp.enums.UserRole;
import com.temitope.myblogapp.model.User;
import com.temitope.myblogapp.repository.UserRepository;
import com.temitope.myblogapp.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@WebMvcTest(BlogControllerTest.class)
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setUsername("temitope");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setFirstName("Temitope");
        testUser.setLastName("Ajayi");
        testUser.setRole(UserRole.EMPLOYEE);

        testUser = userRepository.save(testUser);

        jwtToken = jwtTokenProvider.generateToken(testUser);
    }

    @Test
    void shouldGetAllPublishedBlogs() throws Exception {
        mockMvc.perform(get("/get-all-post"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldCreateBlogWithAuthentication() throws Exception {
        String blogRequest = """
            {
                "title": "Test Blog",
                "content": "This is test content",
                "summary": "Test summary"
            }
            """;

        mockMvc.perform(post("/create-post")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(blogRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Blog"));
    }

    @Test
    void shouldRejectUnauthenticatedBlogCreation() throws Exception {
        String blogRequest = """
            {
                "title": "Test Blog",
                "content": "This is test content",
                "summary": "Test summary"
            }
            """;

        mockMvc.perform(post("/create-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(blogRequest))
                .andExpect(status().isUnauthorized());
    }
}
