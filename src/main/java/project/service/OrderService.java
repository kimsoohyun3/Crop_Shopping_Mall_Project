package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dto.OrderDto;
import project.entity.Item;
import project.entity.Member;
import project.entity.Order;
import project.entity.OrderItem;
import project.repository.ItemImgRepository;
import project.repository.ItemRepository;
import project.repository.MemberRepository;
import project.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;


    // 1-1. 주문
    public Long order(OrderDto orderDto, String email) {
        // 1-2. 주문서에 상품 아이디로 해당 상품을 조회한다.
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 1-3. 현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보를 가져온다.
        Member member = memberRepository.findByEmail(email);

        // 1-4. 주문 목록 리스트 객체 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        // 1-5. 1-2에서 조회한 주문서에 적혀 있는 item 정보를 가져와 저장한 item Entity와 주문서에 적혀 있는 상품 수를 가지고 주문아이템 엔티티를 만든다.
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

        // 1-6. 주문 목록 리스트에 1-5에서 만든 주문아이템 엔티티를 추가
        orderItemList.add(orderItem);

        // 1-7. 회원 정보와 1-5, 1-6에서 만든 주문 상품 리스트 정보를 이용해 주문 엔티티를 생성
        Order order = Order.createOrder(member, orderItemList);

        // 1-8. 저장(영속화) 후 주문 id return
        orderRepository.save(order);

        return order.getId();
    }
}