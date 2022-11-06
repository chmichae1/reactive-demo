package com.example.reactive.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @Column("uuid")
    private String uuid;
    @Column("name")
    private String name;
    @Column("phone")
    private String phone;
    @Column("email")
    private String email;
}
