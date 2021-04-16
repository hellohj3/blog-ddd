package com.portfolio.blog.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AccessIp {

    @Id @Column(name = "access_ip_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String address;

    @Builder
    public AccessIp(Long id, String address) {
        this.id = id;
        this.address = address;
    }
}
