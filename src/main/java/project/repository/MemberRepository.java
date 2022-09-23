package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //아이디 중복검사를 위한 쿼리메소드
    Member findByEmail(String email);
}
