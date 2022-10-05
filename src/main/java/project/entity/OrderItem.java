package project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량

    // 주문할 상품과 주문 수량을 통해 OrderItem객체를 만드는 메소드
    public static OrderItem createOrderItem(Item item, int count) {
        // item과 count로 주문 상품(OrderItem) 객체 생성 및 세팅
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);

        // 상품 가격을 주문 가격으로 세팅.
        orderItem.setOrderPrice(item.getPrice());

        // 상품에 주문 수량을 전달해 재고를 감소 시킨다.(더티체킹)
        item.removeStock(count);

        return orderItem;
    }

    // 총 주문 가격 계산 메소드
    public int getTotalPrice() {
        // 세팅해 놓은 걸로 총 주문 가격 구해서 리턴.
        return orderPrice * count;
    }

    // 취소시 주문 아이템에 있는 count를 수를 다시 증가 시켜준다.
    public void cancel() {
        this.getItem().addStock(count);
    }
}
