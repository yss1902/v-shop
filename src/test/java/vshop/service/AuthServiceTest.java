package vshop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import vshop.crypto.ScryptPasswordEncoder;
import vshop.domain.User;
import vshop.exception.AlreadyExistsEmailException;
import vshop.exception.InvalidSigninInformation;
import vshop.repository.UserRepository;
import vshop.request.Login;
import vshop.request.Signup;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @AfterEach
    void clear(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        //given
        Signup signup = Signup.builder()
                .name("memil")
                .email("memil88@gmail.com")
                .password("1234")
                .build();
        //when
        authService.signup(signup);
        //then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("memil88@gmail.com", user.getEmail());
        assertNotNull(user.getPassword());
        assertEquals("1234", user.getPassword());
        assertEquals("memil", user.getName());
    }
    @Test
    @DisplayName("회원가입시 중복된 이메일이 들어올 때")
    void test2() {
        //given
        User user = User.builder()
                .email("memil88@gmail.com")
                .password("5678")
                .name("momil")
                .build();
        userRepository.save(user);
        Signup signup = Signup.builder()
                .name("memil")
                .email("memil88@gmail.com")
                .password("1234")
                .build();
        //when
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }
    @Test
    @DisplayName("로그인 성공")
    void test3() {
        //given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");
        User user = User.builder()
                .email("memil88@gmail.com")
                .password(encryptedPassword)
                .name("momil")
                .build();
        userRepository.save(user);
        Login login = Login.builder()
                .email("memil88@gmail.com")
                .password("1234")
                .build();
        //when
        Long userId = authService.signin(login);
        //then
        assertNotNull(userId);
    }
    @Test
    @DisplayName("비밀번호 불일치")
    void test4() {
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");
        User user = User.builder()
                .email("memil88@gmail.com")
                .password(encryptedPassword)
                .name("momil")
                .build();
        userRepository.save(user);
        Login login = Login.builder()
                .email("memil88@gmail.com")
                .password("5678")
                .build();
        //then
        assertThrows(InvalidSigninInformation.class, () -> authService.signin(login));
    }
}