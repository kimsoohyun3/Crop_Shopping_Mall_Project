package project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import project.dto.ItemFormDto;
import project.service.ItemService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        //상품 등록 시 필수 값이 없다면 다시 상품 등록페이지로 리다이렉트
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        //상품 등록 시 첫 번째 이미지가 없다면 에러 메시지와 함께 상품 등록 페이지로 리다이렉트(상품의 첫 번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값으로 지정)
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        //상품 등록
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            //상품 등록 실패시 errorMessage를 model에 담아서 itemForm으로 이동
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            //url 파라미터로 전달된 itemId로 조회해서 저장
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            //에러메시지를 담아서 빈 itemFormDto를 가지고 itemForm 페이지로 이동
            model.addAttribute("errorMessage", "존재하지 않은 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        //상품 수정 시 필수 값이 없다면 다시 상품 수정페이지로 리다이렉트
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        //상품 수정 시 첫 번째 이미지가 없다면 에러 메시지와 함께 상품 등록 페이지로 리다이렉트(상품의 첫 번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값으로 지정)
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        //상품 수정
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            //상품 등록 실패시 errorMessage를 model에 담아서 itemForm으로 이동
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }
}
