package vshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import vshop.domain.Item;
import vshop.repository.ItemRepository;
import vshop.request.ItemCreate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("/create 요청시 리턴 내용을 출력한다.")
    void test1() throws Exception {
        //given
        ItemCreate request = ItemCreate.builder()
                .name("제목입니다.")
                .price(1000L)
                .desc("설명입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);
        //expected
        mockMvc.perform(post("/create")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/create 요청시 name값은 필수다.")
    void test2() throws Exception {
        //given
        ItemCreate request = ItemCreate.builder()
                .price(1000L)
                .desc("설명입니다")
                .build();
        String json = objectMapper.writeValueAsString(request);
        //expected
        mockMvc.perform(post("/create")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.name").value("이름을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("/create 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        //given
        ItemCreate request = ItemCreate.builder()
                .name("이름입니다")
                .price(1000L)
                .desc("설명입니다")
                .build();
        String json = objectMapper.writeValueAsString(request);
        //when
        mockMvc.perform(post("/create")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, itemRepository.count());
        Item item = itemRepository.findAll().get(0);
        assertEquals(1000, item.getPrice());
        assertEquals("설명입니다", item.getDesc());
    }

    @Test
    @DisplayName("아이템 1개 조회")
    void test4() throws Exception {
        //given
        Item item = Item.builder()
                .name("123456789012345")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);
        //when
        mockMvc.perform(get("/create/{itemId}", item.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value("1234567890"))
                .andExpect(jsonPath("$.price").value(500L))
                .andExpect(jsonPath("$.desc").value("사과입니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("아이템 여러개 조회")
    void test5() throws Exception {
        //given
        List<Item> requestItems = LongStream.range(0, 20L)
                .mapToObj(i -> Item.builder()
                        .name("아이템 이름 " + i)
                        .price(500 + i)
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(requestItems);
        //when
        //then
        mockMvc.perform(get("/create?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].name").value("아이템 이름 19"))
                .andExpect(jsonPath("$[0].price").value(519))
                .andDo(print());
    }
    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        //given
        List<Item> requestItems = LongStream.range(0, 20L)
                .mapToObj(i -> Item.builder()
                        .name("아이템 이름 " + i)
                        .price(500 + i)
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(requestItems);
        //when
        //then
        mockMvc.perform(get("/create?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].name").value("아이템 이름 19"))
                .andExpect(jsonPath("$[0].price").value(519))
                .andDo(print());
    }
    @Test
    @DisplayName("아이템 제목 수정")
    void test7() throws Exception {
        //given
        Item item = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);

        Item itemEdit = Item.builder()
                .name("포도")
                .price(600L)
                .desc("포도입니다")
                .build();
        //when
        //then
        mockMvc.perform(patch("/create/{itemId}", item.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("아이템 삭제")
    void test8() throws Exception {
        //given
        Item item = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        itemRepository.save(item);
        //when
        //then
        mockMvc.perform(delete("/create/{itemId}", item.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("존재하지 않는 아이템 조회")
    void test9() throws Exception{
        mockMvc.perform(delete("/create/{itemId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @DisplayName("존재하지 않는 아이템 수정")
    void test10() throws Exception{
        //given
        Item itemEdit = Item.builder()
                .name("사과")
                .price(500L)
                .desc("사과입니다")
                .build();
        //when
        mockMvc.perform(patch("/create/{itemId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @DisplayName("아이템 제목에 '수박'는 포함될 수 없음")
    void test11() throws Exception {
        //given
        ItemCreate request = ItemCreate.builder()
                .name("무등산 수박")
                .price(1000L)
                .desc("수박입니다")
                .build();
        String json = objectMapper.writeValueAsString(request);
        //when
        mockMvc.perform(post("/create")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}