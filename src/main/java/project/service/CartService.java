package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.CartDetailDto;
import project.dto.CartItemDto;
import project.entity.Cart;
import project.entity.CartItem;
import project.entity.Item;
import project.entity.Member;
import project.repository.CartItemRepository;
import project.repository.CartRepository;
import project.repository.ItemRepository;
import project.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {  // 장바구니에 상품을 담는 로직 수행하는 서비스

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final OrderService orderService;

    // 장바구니엥 상품을 담는 기능을 수행하는 서비스
    public Long addCart(CartItemDto cartItemDto, String email) {
        // 전달 받은 cartItemDto에 저장된 itemId로 해당 item 엔티티 조회
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 전달 받은 email 주소로 로그인한 회원 엔티티를 조회
        Member member = memberRepository.findByEmail(email);

        // 찾은 회원의 id로 Cart 엔티티를 가져온다.
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 처음 장바구니에 담을 때
        if(cart == null) {
            // 로그인한 회원 엔티티를 가지고 있는 장바구니 엔티티를 새로 생성해준다.
            cart = Cart.createCart(member);

            // 영속화
            cartRepository.save(cart);
        }

        // 처음 장 장바구니에 담는 경우가 아니라면 아이디와 상품 아이디를 가지고 해당  CartItem 엔티티를 가져온다.
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null) { // 장바구니가 비어 있지 않다면
            // 기존 장바구니에 있던 아이템 수량에다가  현재 장바구니에 새로 담을 아이템에 수량을 더해줍니다.
            savedCartItem.addCount(cartItemDto.getCount());

            // 그리고 새로 추가한 아이템을 가지고 있는 장바구니에 id를 return
            return savedCartItem.getId();
        } else {
            // 처음 장바구니에 담는 경우 생성한 장바구니를 가지고 CartItem을 만듭니다.
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());

            // 영속화
            cartItemRepository.save(cartItem);

            // 저장된 CartItem id를 return
            return cartItem.getId();

        }
    }

    // 현재 로그인한 회원의 정보를 이용하여 장바구니에 들어 있는 상품을 조회.
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        // 장바구니에 있는 상품을 담을 CartItemDetailDto 리스트 객체 생성
        List<CartDetailDto> cartItemDetailDtoList = new ArrayList<>();

        // 전달 받은 사용자 이메일로 로그인한 사용자 조회.
        Member member = memberRepository.findByEmail(email);

        // 로그인한 사용자를 가져왔으므로 로그인한 사용자의 id값으로 장바구니 엔티티를 가져온다.
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 장바구니가 비어있다면
        if(cart == null) {
            // 빈 장바구니 상품 리스트 return
            return cartItemDetailDtoList;
        }

        // 비어있지 않다면 장바구니 아이디로 장바구니에 담긴 아이템들 리스트를 가지고온다.
        cartItemDetailDtoList = cartItemRepository.findCartItemDetailDtoList(cart.getId());

        return cartItemDetailDtoList;

    }
}
