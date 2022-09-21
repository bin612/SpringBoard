package com.example.notice.domain.member.repository;

import com.example.notice.domain.member.Member;
import com.example.notice.domain.member.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Test
    public void 성공_회원수정() throws Exception {
        //given
        Member member1 = Member.builder()
            .username("username")
            .password("1234567890")
            .name("Member1")
            .role(Role.USER)
            .nickName("NickName")
            .age(22).build();

        memberRepository.save(member1);
        clear();

        String updatePassword = "패스워드가 수정 되었습니다.";
        String updateName = "이름이 수정 되었습니다.";
        String updateNickName = "닉네임이 수정 되었습니다.";
        int updateAge = 33;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //when
        Member findMember = memberRepository.findById(member1.getId()).orElseThrow(()-> new Exception());
        member1.updateAge(updateAge);
        member1.updateName(updateName);
        member1.updateNicName(updateNickName);
        member1.updatePassword(passwordEncoder, updatePassword);
        em.flush();

        //then
        Member findUpdateMember = memberRepository.findById(findMember.getId()).orElseThrow(()-> new Exception());

        assertThat(findUpdateMember).isSameAs(findMember);
        assertThat(passwordEncoder.matches(updatePassword, findUpdateMember.getPassword()));
        assertThat(findUpdateMember.getName()).isEqualTo(updateName);
        assertThat(findUpdateMember.getName()).isNotEqualTo(member1.getName());
    }

    @Test
    public void 성공_회원삭제() throws Exception {
        //given
        Member member1 = Member.builder()
            .username("username")
            .password("1234567890")
            .name("Member1")
            .role(Role.USER)
            .age(22).build();
        memberRepository.save(member1);
        clear();

        //when
        memberRepository.delete(member1);
        clear();

        //then
        assertThrows(Exception.class, ()-> memberRepository.findById(member1.getId()).orElseThrow(()-> new Exception()));
    }


    /*
    * existByUsername 정상작동 테스트
    * */
    @Test
    public void existByUsername_정상동작() throws Exception {
        //given
        String username = "username";
        Member member1 = Member.builder()
            .username("username")
            .password("1234567890")
            .name("Member1")
            .role(Role.USER)
            .nickName("NickName1")
            .age(22).build();
        memberRepository.save(member1);
        clear();

        //when, then
        assertThat(memberRepository.existByUsername(username)).isTrue();
        assertThat(memberRepository.existByUsername(username+"1234")).isFalse();
    }

    /*
    * findByUsername 정상작동 테스트
    * */
    @Test
    public void findByUsername_정상동작() throws Exception {
        //given
        String username = "username";
        Member member1 = Member.builder()
            .username(username)
            .password("1234567890")
            .name("Member1")
            .role(Role.USER)
            .nickName("NickName1")
            .age(22).build();
        memberRepository.save(member1);
        clear();

        //when, then
        assertThat(memberRepository.findByUsername(username).get().getUsername()).isEqualTo(member1.getUsername());
        assertThat(memberRepository.findByUsername(username).get().getName()).isEqualTo(member1.getName());
        assertThat(memberRepository.findByUsername(username).get().getId()).isEqualTo(member1.getId());
        assertThrows(Exception.class,
                ()-> memberRepository.findByUsername(username+"123")
                    .orElseThrow(()-> new Exception()));
    }

    @Test
    public void 회원가입시_생성시간_등록() throws Exception {
        //given
        Member member1 = Member.builder()
            .username("username")
            .password("1234567890")
            .name("Member1")
            .role(Role.USER)
            .nickName("NickName1")
            .age(22).build();
        memberRepository.save(member1);
        clear();

        //when
        Member findMember = memberRepository.findById(member1.getId()).orElseThrow(()-> new Exception());

        //then
        assertThat(findMember.getCreatedData()).isNotNull();
        assertThat(findMember.getLastModifiedDate()).isNotNull();
    }
}
