package project.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.constant.ItemSellStatus;
import project.constant.OrderStatus;
import project.dto.OrderDto;
import project.entity.Item;
import project.entity.Member;
import project.entity.Order;
import project.entity.OrderItem;
import project.repository.ItemRepository;
import project.repository.MemberRepository;
import project.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    // 주문할 상품 엔티티 저장 후 반환.
    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트물품");
        item.setPrice(10000);
        item.setItemDetail("테스트물품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return itemRepository.save(item);
    }

    // 주문한 회원 엔티티 저장 후 반환
    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    // 주문 테스트
    @Test
    @DisplayName("주문 테스트")
    public void order() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();

        // 주문할 상품과 수량을 OrderDto객체에 세팅
        orderDto.setItemId(item.getId());
        orderDto.setCount(10);

        // 주문 서비스 실행후 주문 아이디 반환 받는다.
        Long orderId = orderService.order(orderDto, member.getEmail());

        // 반환된 주문 아이디로 DB에서 해당 주문을 가져온다
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        // 주문 엔티티에 저장된 주문아이템 정보를 가지고 온다.
        List<OrderItem> orderItems = order.getOrderItems();

        for(OrderItem orderItem : orderItems) {
            System.out.println(orderItem.getItem().getItemNm());
        }

        // 주문한 상품의 총 가격을 구한다.
        int totalPrice = orderDto.getCount() * item.getPrice();

        // 주문한 상품의 총 가격과 데이터베이스에 저장된 상품의 총 가격을 비교하여 같으면 테스트 성공
        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals(100, item.getStockNumber());

    }

}