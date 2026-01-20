package com.example.RecycleProject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
class AuthTest extends BaseTest {

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupSuccess() throws Exception {
        String joinJson = "{" +
                "\"email\":\"newuser@test.com\"," +
                "\"password\":\"password123!\"," +
                "\"passwordCheck\":\"password123!\"," +
                "\"nickname\":\"새유저\"" +
                "}";

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(joinJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 성공 시 토큰을 반환한다")
    void loginSuccess() throws Exception {
        // 먼저 가입
        signupSuccess();

        String loginJson = "{\"email\":\"newuser@test.com\", \"password\":\"password123!\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }
}