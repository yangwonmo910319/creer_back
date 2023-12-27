package com.team.creer_back.repository.member;


import com.team.creer_back.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Member> findByEmailAndPassword(String email, String password);
    boolean existsByNickName(String nickName);
    Optional<Member> findByNickName(String nickName);
    Optional<Member> findByName(String name);
}
