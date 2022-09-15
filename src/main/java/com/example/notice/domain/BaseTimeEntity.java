package com.example.notice.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass // 여러 엔티티가 상속 받을 수 있음
@Getter
public abstract class BaseTimeEntity {

    /*
    * 생성 시간의 데이터
    * */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdData;


    /*
    * 마지막 수정 시간의 데이터
    * */
    @LastModifiedDate
    @Column(updatable = true)
    private LocalDateTime lastModifiedDate;
}
