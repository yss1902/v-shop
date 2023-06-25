package vshop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vshop.domain.Item;
import vshop.exception.ItemNotFound;
import vshop.repository.ItemRepository;
import vshop.request.ItemCreate;
import vshop.request.ItemEdit;
import vshop.request.ItemSearch;
import vshop.response.ItemResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;

    @AfterEach
    void clear(){
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 DB")
    void test1(){
        //given
        ItemCreate itemCreate = ItemCreate.builder()
                .name("이름입니다")
                .price(1000L)
                .desc("설명입니다")
                .build();
        //when
        itemService.save(itemCreate);
        //then
        assertEquals(1L, itemRepository.count());
        Item item = itemRepository.findAll().get(0);
        assertEquals(1000L, item.getPrice());
        assertEquals("설명입니다", item.getDesc());

    }
    @Test
    @DisplayName("아이템 1개 조회")
    void test2() {
        //given
        Item requestItem = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(requestItem);
        //when
        ItemResponse response = itemService.get(requestItem.getId());
        //then
        assertNotNull(response);
        assertEquals("사과", response.getName());
        assertEquals(500L, response.getPrice());
    }

    @Test
    @DisplayName("아이템 첫페이지 조회")
    void test3() {
        //given
        List<Item> requestItems = LongStream.range(0, 20L)
                .mapToObj(i -> Item.builder()
                        .name("아이템 이름 " + i)
                        .price(500 + i)
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(requestItems);

        ItemSearch itemSearch = ItemSearch.builder()
                .page(1)
                .build();
        //when
        List<ItemResponse> items = itemService.getList(itemSearch);
        //then
        assertEquals(10L, items.size());
        assertEquals("아이템 이름 19", items.get(0).getName());
    }
    @Test
    @DisplayName("아이템 이름 수정")
    void test4() {
        //given
        Item item = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);

        ItemEdit itemEdit = ItemEdit.builder()
                .name("포도")
                .price(600L)
                .desc("포도입니다")
                .build();
        //when
        itemService.edit(item.getId(), itemEdit);
        //then
        Item changedItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않음. id = " + item.getId()));
        Assertions.assertEquals("포도", changedItem.getName());
    }
    @Test
    @DisplayName("아이템 가격과 설명 수정")
    void test5() {
        //given
        Item item = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);

        ItemEdit itemEdit = ItemEdit.builder()
                .name("포도")
                .price(600L)
                .desc("포도입니다")
                .build();
        //when
        itemService.edit(item.getId(), itemEdit);
        //then
        Item changedItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않음. id = " + item.getId()));
        Assertions.assertEquals(600L, changedItem.getPrice());
        Assertions.assertEquals("포도입니다", changedItem.getDesc());
    }
    @Test
    @DisplayName("아이템 삭제")
    void test6() {
        //given
        Item item = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);
        //when
        itemService.delete(item.getId());
        //then
        assertEquals(0, itemRepository.count());
    }
    @Test
    @DisplayName("아이템 1개 조회 실패, 예외를 찾을 수 있는가")
    void test7() {
        //given
        Item item = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);
        //then
        assertThrows(ItemNotFound.class, ()->{
            itemService.get(item.getId() + 1L);
        });
    }
    @Test
    @DisplayName("아이템 삭제 실패, 예외를 찾을 수 있는가")
    void test8() {
        //given
        Item item = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);
        //then
        assertThrows(ItemNotFound.class, ()->{
            itemService.delete(item.getId() + 1L);
        });
    }
    @Test
    @DisplayName("아이템 이름 수정하는데 존재하지 않는 아이템 에러")
    void test9() {
        //given
        Item item = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);

        ItemEdit itemEdit = ItemEdit.builder()
                .name("포도")
                .price(600L)
                .desc("포도입니다")
                .build();
        //expected
        assertThrows(ItemNotFound.class, ()->{
            itemService.edit(item.getId() + 1L, itemEdit);
        });
    }
}