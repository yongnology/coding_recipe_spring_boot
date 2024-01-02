package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    //  회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm() {
        System.out.println("★★★ MemberController.saveForm()");
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO)
//                       (@RequestParam("memberEmail") String memberEmail,
//                       @RequestParam("memberPassword") String memberPassword,
//                       @RequestParam("memberName") String memberName)
    {
            System.out.println("★★★MemberController.save");
            System.out.println("★★★memberDTO = " + memberDTO);
//          System.out.println("memberEmail = " + memberEmail + ", memberPassword = " + memberPassword + ", memberName = " + memberName);

        memberService.save(memberDTO);
            System.out.println("★★★ MemberController.save()");
        return "login";
    }

    // 로그인창으로
    @GetMapping("/member/login")
    public String loginForm() {
        System.out.println("★★★ MemberController.loginForm()");
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
            System.out.println("memberDTO = " + memberDTO);
        MemberDTO loginResult = memberService.login(memberDTO);
            System.out.println("loginResult: " + loginResult);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail() );

                System.out.println("★★★loginResult = " + loginResult);
                System.out.println("memberDTO = " + memberDTO + ", session = " + session);
            return "main";
        } else {
            // login 실패
            System.out.println("MemberController.login() 실패");
            System.out.println("memberDTO = " + memberDTO + ", session = " + session);
            return "login";
        }

    }
    // 회원목록
    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        // 어떠한 html로 가져갈 데이터가 있으면 model사용
        model.addAttribute("memberList", memberDTOList);
        return  "list";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);

        if (memberDTO != null) {
                System.out.println("조회 성경, memberDTO = " + memberDTO);
            model.addAttribute("member", memberDTO);
            return "detail";
        } else {
                System.out.println("조회 실패, memberDTO = " + memberDTO);
            return "list";
        }
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String)session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member/" + memberDTO.getId();
    }

    // 회원삭제
    @GetMapping("/member/delete/{id}")
    public String delte(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    // 로그아웃
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @PostMapping("/member/email-check")
       // @ResponseBody   a.jax 와 통신을 위해 필요한 어노테이션
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
//        if(checkResult != null ) {
//            return "ok";
//        } else {
//            return "no";
//        }

    }
}
