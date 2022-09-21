# Spring Board

### 현재까지 경로
```bash
root
├── main
│   ├── generated
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── notice
│   │               ├── NoticeApplication.java
│   │               └── domain
│   │                   ├── BaseTimeEntity.java
│   │                   └── member
│   │                       ├── Member.java
│   │                       ├── Role.java
│   │                       └── repository
│   │                           └── MemberRepository.java
│   └── resources
│       ├── application.yml
│       ├── static
│       └── templates
└── test
    └── java
        └── com
            └── example
                └── notice
                    ├── NoticeApplicationTests.java
                    └── domain
                        └── member
                            └── repository
                                └── MemberRepositoryTest.java
```


- Spring h2 database connection
- testing success

error : Database "/Users/xxxxx/test" not found.... <br>
success
```bash
touch test.mv.db // create file
```

### 회원가입 시 사용 어노테이션 정리
- 객체와 테이블 매핑 : @Entity, @Table
    - @Entity
        - 테이블과의 매핑
        - @Entity가 붙은 클래스는 JPA가 관리하는 것으로, 엔티티라고 불림
        - 속성
            - Name : JPA에서 사용할 엔티티 이름을 지정 보통 기본값인 클래스 이름을 사용
        - 주의사항
            - 기본 생성자는 필수 (JPA가 엔티티 객체 생성 시 기본 생성자를 사용)
            - final 클래스, enum, interface, inner class 에는 사용할 수 없음
            - 저장할 필드에 final 사용 불가
    - @Table
        - 엔티티와 매핑할 테이블을 지정
        - 생략 시 매핑한 엔티티 이름을 테이블 이름으로 사용
        - 속성
            - Name : 매핑할 테이블 이름 (default. 엔티티 이름 사용)
            - Catalog : catalog 기능이 있는 DB에서 catalog를 매핑 (default. DB 명)
            - Schema : schema 기능이 있는 DB에서 schema를 매핑
            - uniqueConstraints : DDL 생성 시 유니크 제약조건을 만듦 스키마 자동 생성 기능을 사용해서 DDL을 만들 때만 사용
- 기본 키 매핑 : @Id
    - @GeneratedValue
        - <기본 키 생성 전략>
        - 직접 할당 : 기본 키를 애플리케이션에 직접 할당
            - em.persist()를 호출하기 전 애플리케이션에서 직접 식별자 값을 할당해야 함. 식별자 값이 없을 경우 에러 발생
        - 자동 생성 : 대리 키 사용 방식
            - IDENTITY : 기본 키 생성을 데이터베이스에 위임 ( = AUTO_INCREMENT)
- 필드와 컬럼 매핑 : @Column
- 연관관계 매핑 : @ManyToOne, @JoinColumn
