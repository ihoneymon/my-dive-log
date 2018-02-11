package me.divelog.core.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * 회원정보 관리
 */
@MappedSuperclass
public class BaseMember<U extends BaseUser> extends BaseEntity {

    @OneToOne
    private U user;

    private String firstName;
    private String lastName;
    private String contactNumber;
    private String org;
    private String level;

    //TODO C-CARD 사진을 업로드하고 보여주는 것도 좋을까??
}
