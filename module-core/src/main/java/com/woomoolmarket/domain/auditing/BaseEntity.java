package com.woomoolmarket.domain.auditing;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends BaseTimeEntity {

  @CreatedBy
  @Column(updatable = false)
  protected String createdBy;

  @LastModifiedBy
  protected String lastModifiedBy;

}

/*
@EntityListeners(value = {AuditingEntityListener.class}) 속성은 상속되지만
@MappedSuperclass 상속 안 되나봄

두 애노테이션 다 정책은 아래와 같은데 뭐가 다르길래 안된걸까?!
@Target({TYPE})
@Retention(RUNTIME)

설명을 조금 찾아보니 매핑 정보를 제공한다는 것을 명시적으로 제공해줘야하는 듯함
 */