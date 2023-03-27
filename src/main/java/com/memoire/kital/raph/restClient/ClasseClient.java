package com.memoire.kital.raph.restClient;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClasseClient {
        private String id;
        private String nom;
        private NiveauClient niveau;
}
