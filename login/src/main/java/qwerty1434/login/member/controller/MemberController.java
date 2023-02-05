package qwerty1434.login.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qwerty1434.login.member.dto.LoginDto;
import qwerty1434.login.member.dto.SignupDto;
import qwerty1434.login.member.service.AuthService;
import qwerty1434.login.member.service.MemberService;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupDto signupDto){
        log.info("signup 시작");
        memberService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){
        log.info("login 시작");
        return ResponseEntity.status(HttpStatus.OK)
                .headers(authService.authorize(loginDto))
                .body(memberService.login(loginDto));
    }

    // 토큰 없이 접근 가능
    @GetMapping("/test")
    public String test(){
        log.info("test 시작");

        return "test_ok";
    }

    // 토큰이 있어야 접근 가능
    @GetMapping("/info")
    public ResponseEntity info(){
        log.info("info 시작");

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.info());
    }

}
