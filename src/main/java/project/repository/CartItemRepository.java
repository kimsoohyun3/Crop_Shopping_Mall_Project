package project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.dto.CartDetailDto;
import project.entity.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Cart 아이디와 Item 아이디를 이용하여 상품이 장바구니에 들어 있는지 조회.
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new project.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc")
    List<CartDetailDto> findCartItemDetailDtoList(@Param("cartId") Long cartId);
}
