package vshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import vshop.domain.Item;
import vshop.repository.ItemRepository;
import vshop.request.ItemCreate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.vshop.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class ItemControllerDocTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("아이템 단건조회 테스트")
    void test1() throws Exception {
        //given
        Item item = Item.builder()
                .name("이름")
                .price(500L)
                .desc("설명")
                .build();
        itemRepository.save(item);
        //expected
        mockMvc.perform(get("/create/{itemId}", 1L).accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("item-inquiry", pathParameters(
                        parameterWithName("itemId").description("아이템 ID")
                ), responseFields(fieldWithPath("id").description("아이템 ID"),
                        fieldWithPath("name").description("아이템 이름"),
                        fieldWithPath("price").description("아이템 가격"),
                        fieldWithPath("desc").description("아이템 설명"))));
    }
    @Test
    @DisplayName("아이템 등록 테스트")
    void test2() throws Exception {
        //given
        ItemCreate request = ItemCreate.builder()
                .name("수1박")
                .price(1000L)
                .desc("수박입니다")
                .build();
        String json = objectMapper.writeValueAsString(request);
        //expected
        mockMvc.perform(post("/create").contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("item-create", requestFields(
                        fieldWithPath("name").description("아이템 이름").attributes(key("constraint")
                                .value("올바른 이름 입력해")),
                        fieldWithPath("price").description("아이템 가격"),
                        fieldWithPath("desc").description("아이템 설명").optional()
                )));
    }
}