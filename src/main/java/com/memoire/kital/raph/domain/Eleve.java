package com.memoire.kital.raph.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.memoire.kital.raph.utils.SizeMapper;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.memoire.kital.raph.domain.enumeration.Sexe;
import org.hibernate.annotations.GenericGenerator;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eleves")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Eleve implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "uuid")
    @Column(name = "id",updatable = false,nullable = false)
    private String id;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Size(min = SizeMapper.SizeMapperEleve.MIN, max = SizeMapper.SizeMapperEleve.MAX)
    @Column(name = "matricule", length = SizeMapper.SizeMapperEleve.MAX)
    private String matricule;

    @NotNull
    @Size(min = SizeMapper.SizeMapperEleve.MIN, max = SizeMapper.SizeMapperEleve.MAX)
    @Column(name = "prenom", length = SizeMapper.SizeMapperEleve.MAX, nullable = false)
    private String prenom;

    @NotNull
    @Size(min = SizeMapper.SizeMapperEleve.MIN, max = SizeMapper.SizeMapperEleve.MAX)
    @Column(name = "nom", length = SizeMapper.SizeMapperEleve.MAX, nullable = false)
    private String nom;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @Lob
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Size(min = SizeMapper.SizeMapperTelephone.MIN, max = SizeMapper.SizeMapperTelephone.MAX)
    @Column(name = "telephone", length = SizeMapper.SizeMapperTelephone.MAX, unique = true)
    private String telephone;

    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @NotNull
    @Column(name = "lieu_naissance", nullable = false)
    private String lieuNaissance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "eleves", allowSetters = true)
    private Tuteur tuteur;
}
