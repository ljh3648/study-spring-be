package com.apple.shop.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    @GetMapping("/signUp")
    public String signUp(Model model) {
        return "signUp.html";
    }

    @PostMapping("/signUp/create")
    public String userCreate(String username, String password, String displayName) {
        memberService.createMember(username,password,displayName);
        return "redirect:/list";
    }

    @GetMapping("/login")
    public String login(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/my-page";
        }
        return "login.html";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication auth) {
//        System.out.println(auth);
//        System.out.println(auth.getName()); //아이디출력가능
//        System.out.println(auth.isAuthenticated()); //로그인여부 검사가능
//        System.out.println(auth.getAuthorities().contains(new SimpleGrantedAuthority("일반유저")));
        return "mypage.html";
    }

    //
    @GetMapping("/user/1")
    @ResponseBody
    public MemberDto getUser(){
        return memberService.getMemberInfo(1L);
    }
}