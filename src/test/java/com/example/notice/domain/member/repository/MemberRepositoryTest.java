package com.example.notice.domain.member.repository;

import com.example.notice.domain.member.Member;
import com.example.notice.domain.member.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    private void clear() {
        em.flush();
        em.clear();
    }

    @AfterEach
    private void after() {
        em.clear();
    }

    @Test
    public void 회원저장_성공() throws Exception {
        //given
        Member member = Member.builder().username("username").password("1234567890").name("Member1").nickName("NickName1").role(Role.USER).age(22).build();

        //when
        Member saveMember = memberRepository.save(member);

        //then
        // 예외 클래스를 만들지 않았기 때문에 RuntimeException 사용
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(() -> new RuntimeException("저장된 회원이 없습니다."));

        assertThat(findMember).isSameAs(saveMember);
        assertThat(findMember).isSameAs(member);
    }

    @Test
    public void 오류_회원가입시_아이디가_없음() throws Exception {

        //given
        Member member = Member.builder()
                                .password("1234567890")
                                .name("Member1")
                                .nickName("NickName1")
                                .role(Role.USER)
                                .age(22).build();

        //when, then
        assertThrows(Exception.class, ()-> memberRepository.save(member));
    }

    @Test
    public void 오류_회원가입시_닉네임이_없음() throws Exception {

        //given
        Member member = Member.builder()
                                .username("username")
                                .password("1234567890")
                                .name("Member1")
                                .role(Role.USER)
                                .age(22).build();

        //when, then
        assertThrows(Exception.class, ()-> memberRepository.save(member));
    }

    @Test
    public void 오류_회원가입시_나이가_없음() throws Exception {
        //given
        Member member = Member.builder()
                                .username("username")
                                .password("1234567890")
                                .name("Member1")
                                .role(Role.USER)
                                .nickName("NickName1").build();


        //when, then
        assertThrows(Exception.class, ()-> memberRepository.save(member));
    }

    @Test
    public void 오류_회원가입시_중복된_아이디가_있음() throws Exception {
        //given
        Member member1 = Member.builder()
            .username("username")
            .password("1234567890")
            .name("Member1")
            .role(Role.USER)
            .nickName("NickName1")
            .age(22).build();

        Member member2 = Member.builder()
            .username("username")
            .password("0000000000")
            .name("Member2")
            .role(Role.USER)
            .nickName("NickName2")
            .age(22).build();

        memberRepository.save(member1);
        clear();

        //when, then
        assertThrows(Exception.class, ()-> memberRepository.save(member2));
    }
}
