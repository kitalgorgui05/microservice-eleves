package com.memoire.kital.raph.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.memoire.kital.raph.domain.enumeration.Sexe;
import lombok.*;

/**
 * A DTO for the {@link com.memoire.kital.raph.domain.Tuteur} entity.
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TuteurDTO implements Serializable {
    private String id;
    private String prenom;
    private String nom;
    private String adresse;
    private String email;
    private Sexe sexe;

    public TuteurDTO(String id) {
        this.id = id;
    }

    public TuteurDTO(String id, String prenom, String nom, String adresse, String email, Sexe sexe) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.adresse = adresse;
        this.email = email;
        this.sexe = sexe;
    }
}
