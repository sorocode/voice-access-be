package com.sorocode.voice_access_be_demo.admin;

import com.sorocode.voice_access_be_demo.enter_log.entity.EnterLog;
import com.sorocode.voice_access_be_demo.enter_log.service.EnterLogService;
import com.sorocode.voice_access_be_demo.member.dto.PatchRequestDto;
import com.sorocode.voice_access_be_demo.member.entity.Member;
import com.sorocode.voice_access_be_demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminViewController {

    private final MemberService memberService;
    private final EnterLogService enterLogService;

    @GetMapping("/members")
    public String memberList(@RequestParam(value = "name", required = false) String name, Model model) {
        List<Member> members;
        if (name != null && !name.isEmpty()) {
            members = memberService.getMembersByUsername(name);
        } else {
            members = memberService.getMembers();
        }
        model.addAttribute("members", members);
        model.addAttribute("keyword", name);
        return "admin/member-list";
    }

    @GetMapping("/logs")
    public String enterLogList(Model model) {
        List<EnterLog> logs = enterLogService.getAllLogs();
        model.addAttribute("logs", logs);
        return "admin/log-list";
    }

    @GetMapping("")
    public String welcome() {
        return "admin/welcome";
    }

    @GetMapping("/members/{id}/logs")
    public String memberLogs(@PathVariable String id, Model model) {
        Member member = memberService.getMemberById(id);
        List<EnterLog> logs = enterLogService.getMemberLogs(id);
        model.addAttribute("member", member);
        model.addAttribute("logs", logs);
        return "admin/member-log-list";
    }

    @PostMapping("/members/{id}/delete")
    public String deleteMember(@PathVariable String id) {
        memberService.deleteMemberById(id);
        return "redirect:/admin/members";
    }

    @GetMapping("/members/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Member member = memberService.getMemberById(id);
        model.addAttribute("member", member);
        return "admin/edit-member";
    }

    @PostMapping("/members/{id}/edit")
    public String updateMember(@PathVariable String id, @ModelAttribute PatchRequestDto dto) {
        memberService.updateMember(id, dto);
        return "redirect:/admin/members";
    }
}