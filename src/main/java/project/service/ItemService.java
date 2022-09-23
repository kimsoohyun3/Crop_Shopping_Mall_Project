package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.dto.ItemFormDto;
import project.dto.ItemImgDto;
import project.entity.Item;
import project.entity.ItemImg;
import project.repository.ItemImgRepository;
import project.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    ////상품 등록
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        //itemFormDto를 item entity로 변환
        Item item = itemFormDto.createItem();
        //영속화
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            //첫번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 세팅하고 나머지 상품 이미지는 "N"으로 설정
            if (i == 0) {
                itemImg.setRepimgYn("Y");
            } else {
                itemImg.setRepimgYn("N");
                itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
            }
        }

        return item.getId();
    }

    //상품 단건 조회
    @Transactional(readOnly = true) //전체 서비스에 트랜잭션을 걸어두고 해당 서비스에 트랜잭션을 또 걸고 readonly를 true로 해주면 JPA가 더티체킹을 수행하지 않아 성능이 향상된다.
    public ItemFormDto getItemDtl(Long itemId) {
        //매개변수로 전달된 itemId로 등록된 이미지 리스트 정보 가져오기.
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        //Dto로 변환
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        //itemImgDtoList에 추가.
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    //상품 업데이트
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        //상품 수정을 위해 전달 받은 itemFormDto에서 id값을 꺼내 item 엔티티 조회
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        //상품 수정(변경감지)
        item.updateItem(itemFormDto);

        //등록된 이미지들의 id값을 가져온다.
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }
}
