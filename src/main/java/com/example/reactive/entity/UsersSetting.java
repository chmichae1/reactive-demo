package com.example.reactive.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersSetting {
    @Column("uuid")
    private String uuid;
    @Column("active")
    private boolean active;
    @Column("verify")
    private boolean verify;
    @Column("lock")
    private boolean lock;
}
