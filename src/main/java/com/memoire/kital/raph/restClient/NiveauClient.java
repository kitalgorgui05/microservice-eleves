package com.memoire.kital.raph.restClient;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NiveauClient implements Serializable {
    private String id;
    private String nom;
}
