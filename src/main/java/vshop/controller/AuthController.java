package vshop.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vshop.config.AppConfig;
import vshop.request.Login;
import vshop.request.Signup;
import vshop.response.SessionResponse;
import vshop.service.AuthService;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){
        // json 아이디/비밀번호 -> DB조회 -> 토큰을 응답
        Long userId = authService.signin(login);
        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());
        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .setIssuedAt(new Date())
                .compact();
        return new SessionResponse(jws);
    }
    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
