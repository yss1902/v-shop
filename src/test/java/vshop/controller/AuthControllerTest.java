package vshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import vshop.domain.Session;
import vshop.domain.User;
import vshop.repository.SessionRepository;
import vshop.repository.UserRepository;
import vshop.request.Login;
import vshop.request.Signup;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {
        //given
        userRepository.save(User.builder()
                .name("memil")
                .email("memil88@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("memil88@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.name").value("이름을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 섹션 1개 발급")
    void test2() throws Exception {
        //given(회원가입 -> 로그인 -> 사용자 ID 조회 -> 섹션 수 비교)
        User user = userRepository.save(User.builder()
                .name("memil")
                .email("memil88@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("memil88@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(1L, user.getSessions().size());
    }
    @Test
    @Transactional
    @DisplayName("로그인 성공 후 섹션 응답")
    void test3() throws Exception {
        //given(회원가입 -> 로그인 -> 사용자 ID 조회 -> 섹션 수 비교)
        User user = userRepository.save(User.builder()
                .name("memil")
                .email("memil88@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("memil88@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(print());

        Assertions.assertEquals(1L, user.getSessions().size());
    }
    @Test
    @Transactional
    @DisplayName("로그인 성공 후 필요한 페이지 접속 /foo")
    void test4() throws Exception {
        User user = userRepository.save(User.builder()
                .name("memil")
                .email("memil88@gmail.com")
                .password("1234")
                .build());
        Session session = user.addSession();
        userRepository.save(user);

        Login login = Login.builder()
                .email("memil88@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @Transactional
    @DisplayName("로그인 성공 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        User user = User.builder()
                .name("memil")
                .email("memil88@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);
        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "ruiningwork")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
    @Test
    @Transactional
    @DisplayName("회원가입")
    void test6() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("memil88@gmail.com")
                .password("5678")
                .name("memil")
                .build();
        //expected
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}