package project.dto;

import lombok.Getter;
import lombok.Setter;
import project.constant.OrderStatus;
import project.entity.Order;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {

    // 주문 아이디
    private Long orderId;

    // 주문 날짜
    private String orderDate;

    // 주문 상태
    private OrderStatus orderStatus;

    // 주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    // 생성자
    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS"));
        this.orderStatus = order.getOrderStatus();
    }

    // orderItemDto 객체를 주문 상품 리스트(1-4)에 추가하는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}
