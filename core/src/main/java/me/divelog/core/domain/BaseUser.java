package me.divelog.core.domain;

import javax.persistence.MappedSuperclass;

/**
 * 서비스를 이용하는 사용자
 */
@MappedSuperclass
public class BaseUser extends BaseEntity {

    private String email;
    private String password;

}
