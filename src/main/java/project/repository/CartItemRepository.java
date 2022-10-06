package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Cart 아이디와 Item 아이디를 이용하여 상품이 장바구니에 들어 있는지 조회.
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
}
