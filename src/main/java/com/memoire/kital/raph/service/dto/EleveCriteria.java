package com.memoire.kital.raph.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.memoire.kital.raph.domain.enumeration.Sexe;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.memoire.kital.raph.domain.Eleve} entity. This class is used
 * in {@link com.memoire.kital.raph.web.rest.EleveResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /eleves?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EleveCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Sexe
     */
    public static class SexeFilter extends Filter<Sexe> {

        public SexeFilter() {
        }

        public SexeFilter(SexeFilter filter) {
            super(filter);
        }

        @Override
        public SexeFilter copy() {
            return new SexeFilter(this);
        }

    }

    private StringFilter id;

    private StringFilter matricule;

    private StringFilter prenom;

    private StringFilter nom;

    private SexeFilter sexe;

    private StringFilter telephone;

    private StringFilter email;

    private LocalDateFilter dateNaissance;

    private StringFilter lieuNaissance;

    private StringFilter tuteurId;

    public EleveCriteria() {
    }

    public EleveCriteria(EleveCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matricule = other.matricule == null ? null : other.matricule.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.dateNaissance = other.dateNaissance == null ? null : other.dateNaissance.copy();
        this.lieuNaissance = other.lieuNaissance == null ? null : other.lieuNaissance.copy();
        this.tuteurId = other.tuteurId == null ? null : other.tuteurId.copy();
    }

    @Override
    public EleveCriteria copy() {
        return new EleveCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getMatricule() {
        return matricule;
    }

    public void setMatricule(StringFilter matricule) {
        this.matricule = matricule;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public StringFilter getNom() {
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public SexeFilter getSexe() {
        return sexe;
    }

    public void setSexe(SexeFilter sexe) {
        this.sexe = sexe;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDateFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public StringFilter getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(StringFilter lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public StringFilter getTuteurId() {
        return tuteurId;
    }

    public void setTuteurId(StringFilter tuteurId) {
        this.tuteurId = tuteurId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EleveCriteria that = (EleveCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(matricule, that.matricule) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(lieuNaissance, that.lieuNaissance) &&
            Objects.equals(tuteurId, that.tuteurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        matricule,
        prenom,
        nom,
        sexe,
        telephone,
        email,
        dateNaissance,
        lieuNaissance,
        tuteurId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EleveCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (matricule != null ? "matricule=" + matricule + ", " : "") +
                (prenom != null ? "prenom=" + prenom + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (telephone != null ? "telephone=" + telephone + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (dateNaissance != null ? "dateNaissance=" + dateNaissance + ", " : "") +
                (lieuNaissance != null ? "lieuNaissance=" + lieuNaissance + ", " : "") +
                (tuteurId != null ? "tuteurId=" + tuteurId + ", " : "") +
            "}";
    }

}
