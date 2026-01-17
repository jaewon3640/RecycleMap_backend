package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.JoinRequest;
import com.example.RecycleProject.DTO.LoginRequest;
import com.example.RecycleProject.Repository.UserRepository;
import com.example.RecycleProject.domain.Role;
import com.example.RecycleProject.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 최적화
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 (쓰기 트랜잭션)
    /*
     이떄 암호를 평문으로 하면 위험하기 떄문에 비밀번호는 암호화
     해서 저장하자(리팩토링)
     */
    @Transactional
    public Long join(JoinRequest dto) {
        validateDuplicate(dto.getEmail());

        User user = dto.toEntity();
        // 가입 시 기본 권한 설정 (예: USER)
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // BCrypt 저장
        return userRepository.save(user).getId();
    }

    // 로그인
    /*
        로그인을 실패하면 exception을 터트리고 성공시 User 객체를 반환한다.
       중요: 암호화된 비밀번호는 matches 메서드로 비교해야 합니다.

     */
    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    // 중복이면 예외로 막기
    private void validateDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    // 회원 전체 조회
    public List<User> findMembers() {
        return userRepository.findAll();
    }

    // 회원 단건 조회
    public User findOne(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
    }
}
