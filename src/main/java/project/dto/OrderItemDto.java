package project.dto;

import lombok.Getter;
import lombok.Setter;
import project.entity.OrderItem;

@Getter
@Setter
public class OrderItemDto {

    // 상품명
    private String itemNm;

    // 주문 수량
    private int count;

    // 주문 금액
    private int orderPrice;

    // 상품 이미지 경로
    private String imgUrl;

    // orderItem 엔티티와 이미지 경로를 파라미터로 받아서 OrderItemDto 멤버 변수들 초기화
    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
