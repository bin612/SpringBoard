package com.example.notice.domain.member;

import com.example.notice.domain.BaseTimeEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; //primary key

    @Column(nullable = false, length = 30, unique = true)
    private String username; // 아이디

    private String password; // 비밀번호

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30)
    private String nickName; // 별명

    @Column(nullable = false, length = 30)
    private Integer age; //나이

    @Enumerated(EnumType.STRING)
    private Role role; // 권한 -> USER, ADMIN


    //== 정보 수정 ==//
    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNicName(String nickName) {
        this.nickName = nickName;
    }

    public void updateAge(int age) {
        this.age = age;
    }


    /*
    * 패스워드 암호화
    * */
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /*
    * 권한 부여
    * */

    public void addUserAuthority() {
        this.role = Role.USER;
    }
}


