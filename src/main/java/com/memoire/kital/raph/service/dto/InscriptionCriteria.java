package com.memoire.kital.raph.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.memoire.kital.raph.domain.enumeration.Etat;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.memoire.kital.raph.domain.Inscription} entity. This class is used
 * in {@link com.memoire.kital.raph.web.rest.InscriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inscriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InscriptionCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Etat
     */
    public static class EtatFilter extends Filter<Etat> {

        public EtatFilter() {
        }

        public EtatFilter(EtatFilter filter) {
            super(filter);
        }

        @Override
        public EtatFilter copy() {
            return new EtatFilter(this);
        }

    }
    private StringFilter id;

    private InstantFilter dateInscription;

    private StringFilter classe;

    private BooleanFilter transport;

    private BooleanFilter cantine;

    private EtatFilter statut;

    private StringFilter eleveId;

    private StringFilter anneeId;

    public InscriptionCriteria() {
    }

    public InscriptionCriteria(InscriptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateInscription = other.dateInscription == null ? null : other.dateInscription.copy();
        this.classe = other.classe == null ? null : other.classe.copy();
        this.transport = other.transport == null ? null : other.transport.copy();
        this.cantine = other.cantine == null ? null : other.cantine.copy();
        this.statut = other.statut == null ? null : other.statut.copy();
        this.eleveId = other.eleveId == null ? null : other.eleveId.copy();
        this.anneeId = other.anneeId == null ? null : other.anneeId.copy();
    }

    @Override
    public InscriptionCriteria copy() {
        return new InscriptionCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public InstantFilter getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(InstantFilter dateInscription) {
        this.dateInscription = dateInscription;
    }

    public StringFilter getClasse() {
        return classe;
    }

    public void setClasse(StringFilter classe) {
        this.classe = classe;
    }

    public BooleanFilter getTransport() {
        return transport;
    }

    public void setTransport(BooleanFilter transport) {
        this.transport = transport;
    }

    public BooleanFilter getCantine() {
        return cantine;
    }

    public void setCantine(BooleanFilter cantine) {
        this.cantine = cantine;
    }

    public EtatFilter getStatut() {
        return statut;
    }

    public void setStatut(EtatFilter statut) {
        this.statut = statut;
    }

    public StringFilter getEleveId() {
        return eleveId;
    }

    public void setEleveId(StringFilter eleveId) {
        this.eleveId = eleveId;
    }

    public StringFilter getAnneeId() {
        return anneeId;
    }

    public void setAnneeId(StringFilter anneeId) {
        this.anneeId = anneeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InscriptionCriteria that = (InscriptionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateInscription, that.dateInscription) &&
            Objects.equals(classe, that.classe) &&
            Objects.equals(transport, that.transport) &&
            Objects.equals(cantine, that.cantine) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(eleveId, that.eleveId) &&
            Objects.equals(anneeId, that.anneeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateInscription,
        classe,
        transport,
        cantine,
        statut,
        eleveId,
        anneeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateInscription != null ? "dateInscription=" + dateInscription + ", " : "") +
                (classe != null ? "classe=" + classe + ", " : "") +
                (transport != null ? "transport=" + transport + ", " : "") +
                (cantine != null ? "cantine=" + cantine + ", " : "") +
                (statut != null ? "statut=" + statut + ", " : "") +
                (eleveId != null ? "eleveId=" + eleveId + ", " : "") +
                (anneeId != null ? "anneeId=" + anneeId + ", " : "") +
            "}";
    }

}
