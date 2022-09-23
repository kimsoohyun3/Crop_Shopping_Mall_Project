package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //상품명
    List<Item> findByItemNm(String itemNm);

    //OR 조건 처리
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    //LessThan 조건 처리 - 파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드
    List<Item> findByPriceLessThan(Integer price);

    //OrderBy로 정렬 처리(기본은 ASC)
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //@Query를 이용한 검색 처리하기(JPQL)
    @Query("select i from Item i where i.itemDetail like :itemDetail order by i.price desc ")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
}
