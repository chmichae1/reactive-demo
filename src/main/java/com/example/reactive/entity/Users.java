package com.example.reactive.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Persistable{
    @Id
    @Column("uuid")
    private String uuid;
    @Column("name")
    private String name;
    @Column("phone")
    private String phone;
    @Column("email")
    private String email;

    @Transient
    @JsonIgnore
    private boolean newUser;

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return this.newUser || uuid == null;
    }

    public Users setAsNew(){
        this.newUser = true;
        return this;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return uuid;
    }
}
