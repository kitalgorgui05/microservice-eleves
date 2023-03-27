package com.memoire.kital.raph.service.dto;

import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link com.memoire.kital.raph.domain.Eleve} entity.
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EleveDTOReq implements Serializable {
    private String id;
    private String prenom;
    private String nom;
}
