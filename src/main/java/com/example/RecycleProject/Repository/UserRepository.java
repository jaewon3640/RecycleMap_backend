package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 구현 기능은? 회원 등록 + 회원 목록 조회

    //email을 통해서 Member 1명을 조회하는 메서드
    Optional<User> findByEmail(String email);

    //해당 email이 존재하는지의 대한 유무, 회원가입시 중복 체크를 위해 필요
    boolean existsByEmail(String email);

}
