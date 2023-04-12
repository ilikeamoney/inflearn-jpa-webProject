package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberRepository memberRepository;

    @Autowired MemberService memberService;

    @Test
    @Rollback(value = false)
    void createMember() {
        //given
        Member member = new Member();
        member.setName("userA");

        //when
        Long saveMember = memberService.join(member);

        //then
        assertEquals(member, memberRepository.find(saveMember));
    }

    @Test void duplicate_Member_ex() {
        //given
        Member member1 = new Member();
        member1.setName("userA");

        Member member2 = new Member();
        member2.setName("userA");

        //when
        memberService.join(member1);

        //then
        assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class);
    }

}