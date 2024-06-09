package pinting.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pinting.backend.config.SecurityConfig;
import pinting.backend.entity.Member;
import pinting.backend.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {

	private final MemberRepository memberRepository;

	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	/**
	 * 회원가입 */
	public Long join(Member member) {
		validateDuplicateMember(member); //중복 회원 검증
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplicateMember(Member member) {
		memberRepository.findByName(member.getName())
				.ifPresent(m -> {
					throw new IllegalStateException("이미 존재하는 회원입니다.");
				}); }

	/**
	 * 전체 회원 조회 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}
	public Optional<Member> findOne(Long memberId) {
		return memberRepository.findById(memberId);
	}
}
