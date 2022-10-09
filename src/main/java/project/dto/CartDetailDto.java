package project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {
    // 장바구니 상품 아이디
    private Long cartItemId;

    // 상품명
    private String itemNm;

    // 상품 가격
    private int price;

    // 수량
    private int count;

    // 상품 이미지 경로
    private String imgUrl;

    // 생성자
    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
}
