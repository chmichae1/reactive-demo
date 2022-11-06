package com.example.reactive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PokemonResponse {
    private int id;
    private String name;
    @JsonProperty("base_experience")
    private String baseExperience;
    private String height;
    private int order;
    private int weight;
}
