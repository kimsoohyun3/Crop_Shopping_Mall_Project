package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.entity.ItemImg;
import project.repository.ItemImgRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    //@Value 어노테이션(org.springframework.beans.factory.annotation.Value) -> application.properties에 설정한 "itemImgLocation" 프로퍼티 값을 읽어옵니다.
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            //itemName : 실제 로컬에 저장된 상품 이미지 파일의 이름, oriImgName : 업로드 했던 상품 이미지 파일의 원래 이름
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            //imgUrl : 업로드 결과 로컬에 저장된 상품이미지 파일을 불러오는 요청 경로
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    //상품 이미지 수정
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        //상품 이미지를 수정하는 경우 상품 이미지를 업데이트 한다.
        if (!itemImgFile.isEmpty()) {
            //저장된 이미지를 가져온다.
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            //전달 받은 MultipartFile itemImgFile의 원본 파일명을 가지고온다.
            String oriImgName = itemImgFile.getOriginalFilename();
            //새로운 이미지를 업데이트 해준다.
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            //변경된 상품이미지 경로를 만들어 준다.
            String imgUrl = "/images/item/" + imgName;
            //엔티티에 만들어 놓은 updateItemImg 메소드를 이용해 변경감지(더티체킹)를 일으켜 update 쿼리를 실행시킨다.
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
