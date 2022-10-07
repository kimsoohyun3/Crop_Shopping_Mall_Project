package project.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.constant.ItemSellStatus;
import project.dto.CartItemDto;
import project.entity.CartItem;
import project.entity.Item;
import project.entity.Member;
import project.repository.CartItemRepository;
import project.repository.ItemRepository;
import project.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    // 장바구니에 담을 상품 생성 메소드
    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("윈도우 CD-KEY");
        item.setPrice(10000);
        item.setItemDetail("윈도우10 Professial");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(10);
        return itemRepository.save(item);
    }

    // 장바구니를 이용하는 회원 생성 메소드
    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test1234@google.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart() {
        Item item = saveItem();
        Member member = saveMember();

        // 장바구니에 담을 상품의 itemId와 수량을 CartItemDto에 셋팅
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(item.getId());
        cartItemDto.setCount(5);

        // CartItemDto랑, 주문자 이메일로 장바구니를 만든다.
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        // 장바구니에 등록된 cartItemId로 해당 장바구니 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        // 해당 아이템 id와 장바구니에 저장된 아이템 id를 비교
        assertEquals(item.getId(), cartItem.getItem().getId());

        // 장바구니에 담았던 수량ㅇ과 실제로 장바구니에 저장된 상품 수량 비교.
        assertEquals(cartItemDto.getCount(), cartItem.getCount());

    }


}