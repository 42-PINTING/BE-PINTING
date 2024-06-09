package pinting.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pinting.backend.entity.Member;
import pinting.backend.controller.dto.MemberDto;
import pinting.backend.service.MemberService;

import java.util.List;

@Controller
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/members/new")
	public String createForm() {
		return "members/createMemberForm";
	}

	@PostMapping("/members/new")
	public String create(MemberDto memberDto) {
		Member member = new Member();
		member.setName(memberDto.getName());

		memberService.join(member);

		return "redirect:/members/admin";
	}

	@GetMapping("/members")
	public String list(Model model) {
		List<Member> members = memberService.findMembers();

		model.addAttribute("members", members);
		return "members/memberList";
	}

	@GetMapping("/members/admin")
	public String memberAdmin(Model model) {
		return "member";
	}
}
