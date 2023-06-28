package vshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vshop.config.data.UserSession;
import vshop.request.ItemCreate;
import vshop.request.ItemEdit;
import vshop.request.ItemSearch;
import vshop.response.ItemResponse;
import vshop.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/foo")
    public Long test(UserSession userSession){
        log.info(">>>{}", userSession.id);
        return userSession.id;
    }

    @GetMapping("/bar")
    public String test2(){
        return "인증이 필요없는 페이지입니다.";
    }

    @PostMapping("/create")
    public void create(@RequestBody @Valid ItemCreate request){
        request.validate();
        itemService.save(request);
    }

    @GetMapping("/create/{itemId}")
    public ItemResponse get(@PathVariable Long itemId){
        return itemService.get(itemId);
    }

    @GetMapping("/create")
    public List<ItemResponse> getList(@ModelAttribute ItemSearch itemSearch){
        return itemService.getList(itemSearch);
    }

    @PatchMapping("/create/{itemId}")
    public void edit(@PathVariable Long itemId, @RequestBody @Valid ItemEdit request){
        itemService.edit(itemId, request);
    }

    @DeleteMapping("/create/{itemId}")
    public void delete(@PathVariable Long itemId){
        itemService.delete(itemId);
    }
}
