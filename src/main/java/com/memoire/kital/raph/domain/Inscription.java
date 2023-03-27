package com.memoire.kital.raph.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.memoire.kital.raph.restClient.ClasseClient;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.memoire.kital.raph.domain.enumeration.Etat;
import org.hibernate.annotations.GenericGenerator;

/**
 * A Inscription.
 */
@Entity
@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inscriptions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inscription implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "uuid")
    @Column(name = "id",nullable = false,updatable = false)
    private String id;

    @NotNull
    @Column(name = "date_inscription", nullable = false)
    private Instant dateInscription;

    @NotNull
    @Column(name = "classe", nullable = false)
    private String classe;

    @Transient
    private ClasseClient classeClient;

    @Column(name = "transport")
    private Boolean transport;

    @Column(name = "cantine")
    private Boolean cantine;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private Etat statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "inscriptions", allowSetters = true)
    private Eleve eleve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "inscriptions", allowSetters = true)
    private Annee annee;

    public boolean isCantine() {
        return false;
    }

    public boolean isTransport() {
        return false;
    }
}
