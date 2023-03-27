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

/**
 * Criteria class for the {@link com.memoire.kital.raph.domain.Tuteur} entity. This class is used
 * in {@link com.memoire.kital.raph.web.rest.TuteurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tuteurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TuteurCriteria implements Serializable, Criteria {
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

    private StringFilter prenom;

    private StringFilter nom;

    private StringFilter email;

    private SexeFilter sexe;

    public TuteurCriteria() {
    }

    public TuteurCriteria(TuteurCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
    }

    @Override
    public TuteurCriteria copy() {
        return new TuteurCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
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

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public SexeFilter getSexe() {
        return sexe;
    }

    public void setSexe(SexeFilter sexe) {
        this.sexe = sexe;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TuteurCriteria that = (TuteurCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(email, that.email) &&
            Objects.equals(sexe, that.sexe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        prenom,
        nom,
        email,
        sexe
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TuteurCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (prenom != null ? "prenom=" + prenom + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
            "}";
    }

}
