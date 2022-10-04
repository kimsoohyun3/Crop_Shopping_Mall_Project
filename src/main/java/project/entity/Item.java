package project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.constant.ItemSellStatus;
import project.dto.ItemFormDto;
import project.exception.OutOfStockException;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @Column(name = "item_id")
    @GeneratedValue
    private Long id; //상품코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    //    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    //상품 업데이트 기능
    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 상품 주문시 재고 감소시키는 기능
    public void removeStock(int stockNumber) {
        // 남은 재고 = 현재 재고 - 주문 수량
        int restStock = this.stockNumber - stockNumber;

        // 남은 재고가 0보다 작으면
        if(restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다(현재 재고 수량 : " + this.stockNumber + ")");
        }

        // 남은 재고량으로 세팅.
        this.stockNumber = restStock;
    }

    // 상품 주문시 다시 재고 증가시키는 기능
    public void addStock(int stockNumber) {
        // 주문했던 수량만큼 다시 증가시켜준다.
        this.stockNumber += stockNumber;
    }
}
