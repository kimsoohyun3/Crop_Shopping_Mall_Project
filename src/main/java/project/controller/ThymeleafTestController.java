package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafTestController {

    @GetMapping(value = "/test01")
    public String thymeleafTest01(Model model) {
        model.addAttribute("data", "타임리프 공부 테스트입니다.");
        return "thymeleafTest/thymeleafTest01";
    }

    @GetMapping(value = "/test02")
    public String thymeleafTest02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafTest/thymeleafTest02";
    }

    @GetMapping(value = "/test03")
    public String thymeleafTest03(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafTest/thymeleafTest03";
    }

    @GetMapping(value = "/test04")
    public String thymeleafTest04(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafTest/thymeleafTest04";
    }

    @GetMapping(value = "/test05")
    public String thymeleafTest05(Model model) {
        return "thymeleafTest/thymeleafTest05";
    }

    @GetMapping(value = "/test06")
    public String thymeleafTest06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafTest/thymeleafTest06";
    }

    @GetMapping(value = "/test07")
    public String thymeleafTest07() {
        return "thymeleafTest/thymeleafTest07";
    }

}
