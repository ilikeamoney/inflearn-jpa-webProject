package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 트랜잭션 안에서 데이터 변경이 이뤄져야한다.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        repository.save(member);
        return member.getId();
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return repository.findAll();
    }

    /**
     * 회원 한명 조회
     */
    public Member findOne(Long id) {
        return repository.find(id);
    }

    /**
     * 중복 회원 이름 검증
     */
    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = repository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이름 입니다.");
        }
    }
}
