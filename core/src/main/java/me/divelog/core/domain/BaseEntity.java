package me.divelog.core.domain;

import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity extends AbstractPersistable<Long> {
}
