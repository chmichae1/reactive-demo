package com.example.reactive.model;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String phone;
    private String email;
}
