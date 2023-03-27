package com.memoire.kital.raph.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.memoire.kital.raph.domain.enumeration.Sexe;
import lombok.*;

/**
 * A DTO for the {@link com.memoire.kital.raph.domain.Eleve} entity.
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class EleveDTO implements Serializable {
    private String id;
    private byte[] photo;
    private String photoContentType;
    private String matricule;
    private String prenom;
    private String nom;
    private Sexe sexe;
    private String adresse;
    private String telephone;
    private String email;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private TuteurDTO tuteur;
    public EleveDTO() {
    }
    public EleveDTO(String id, byte[] photo, String photoContentType, String matricule, String prenom, String nom, Sexe sexe, String adresse, String telephone, String email, LocalDate dateNaissance, String lieuNaissance, TuteurDTO tuteur) {
        this.id = id;
        this.photo = photo;
        this.photoContentType = photoContentType;
        this.matricule = matricule;
        this.prenom = prenom;
        this.nom = nom;
        this.sexe = sexe;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.tuteur = tuteur;
    }

    public EleveDTO(String id) {
        this.id = id;
    }
}
