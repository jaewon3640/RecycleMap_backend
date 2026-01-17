package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.LoginRequest;
import com.example.RecycleProject.domain.User;
import com.example.RecycleProject.service.UserService;
import com.example.RecycleProject.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/session")
public class SessionAuthController {

    private final UserService userService;

    // 1) 로그인: 성공하면 세션 생성 + 세션에 userId 저장
    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request, HttpServletRequest httpRequest) {
        User user = userService.login(request);

        HttpSession session = httpRequest.getSession(true); // 세션 없으면 생성헤ㅛㅏ ㄹ;탄
        session.setAttribute(SessionConst.LOGIN_USER_ID, user.getId());
        /*
            왜 세션상수를 쓰는가...? 유저아이디임을 저장하는데 유저아이디임을 표시하기 위함! ENUM과 유사
            근데 여기서 첫번쨰는 String으로 이름이므로 ENUM이 오히려 불편함
         */

        return "세션 로그인 성공";
    }

    // 2) 로그아웃: 세션 삭제
    @PostMapping("/logout")
    public String logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false); // 없으면 null
        if (session != null) {
            session.invalidate();
        }
        return "로그아웃 성공";
    }

    // 3) 내 정보: 세션에서 userId 꺼내서 조회
    @GetMapping("/me")
    public Object me(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null)
            return "로그인 필요";

        Long userId = (Long) session.getAttribute(SessionConst.LOGIN_USER_ID);
        //세션은 있는데 LOGIN USER ID 가 없다면? 로그인 안한 세션일수 있으므로 return
        if (userId == null)
            return "로그인 필요";

        return userService.findOne(userId);
    }
}
