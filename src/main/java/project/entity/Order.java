package project.entity;

import lombok.Getter;
import lombok.Setter;
import project.constant.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문 상품 정보를 담아주는 메소드
    public void addOrderItem(OrderItem orderItem) {
        // orderItems에 주문 상품 정보를 담는다.
        orderItems.add(orderItem);
        // Order 엔티티와 OrderItem 엔티티는 양방향 관계이므로 orderItem 객체에서도 order를 참조할 수 있게 order 객체를 세팅
        orderItem.setOrder(this);
    }

    // 주문 생성 메소드
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();

        // 상품을 주문한 회원 정보를 세팅
        order.setMember(member);

        // 여러개를 주문 할수도 있으므로 리스트형태로 값을 받아서 세팅합니다.
        for(OrderItem orderItem : orderItemList) {
            // 주문 엔티티에 OrderItem(주문상품) 셋팅
            order.addOrderItem(orderItem);
        }

        // 주문 상태 ORDER로 세팅
        order.setOrderStatus(OrderStatus.ORDER);

        // 주문 시간을 현재시간으로 세팅
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 총 주문 금액을 구하는 메소드
    public int getTotalPrice() {
        int totalPrice = 0;

        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }

    // 주문 취소 메소드
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems) {
            // 주문 아이템들을 반복문을 돌며 취소.
            orderItem.cancel();
        }
    }
}
