package com.example.RecycleProject;

import com.example.RecycleProject.Repository.UserRepository;
import com.example.RecycleProject.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;;
class UserRegionTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("로그인한 유저는 자신의 지역을 설정할 수 있다")
    void updateRegionTest() throws Exception {
        // 1. 사전 준비: 유저 가입 및 토큰 획득
        String email = "region@test.com";
        String pass = "password123!";

        // 가입 (간략하게 작성)
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\""+email+"\",\"password\":\""+pass+"\",\"passwordCheck\":\""+pass+"\",\"nickname\":\"지역맨\"}"));

        String token = getAccessToken(email, pass);

        // 2. 지역 설정 요청
        String regionJson = "{\"city\":\"서울특별시\",\"district\":\"강남구\",\"dong\":\"삼성동\"}";

        mockMvc.perform(post("/api/user/region")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regionJson))
                .andExpect(status().isOk());

        // 3. DB 검증
        User user = userRepository.findByEmail(email).get();
        assertEquals("서울특별시", user.getRegion().getCity());
        assertEquals("삼성동", user.getRegion().getDong());
    }
}