package com.memoire.kital.raph.service.dto;

import lombok.*;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.memoire.kital.raph.domain.Annee} entity.
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnneeDTO implements Serializable {
    private String id;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public AnneeDTO(String id) {
        this.id = id;
    }

    public AnneeDTO(String id, String nom, LocalDate dateDebut, LocalDate dateFin) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }
}
