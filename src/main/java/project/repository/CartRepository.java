package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // 현재 로그인한 회원의 Cart 엔티티를 찾기 위해 추가
    Cart findByMemberId(Long memberId);
}
