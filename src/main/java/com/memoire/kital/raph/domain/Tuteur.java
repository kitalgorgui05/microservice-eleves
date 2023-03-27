package com.memoire.kital.raph.domain;

import com.memoire.kital.raph.utils.SizeMapper;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.memoire.kital.raph.domain.enumeration.Sexe;
import org.hibernate.annotations.GenericGenerator;

/**
 * A Tuteur.
 */
@Entity
@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tuteurs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tuteur implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "uuid")
    @Column(name = "id", nullable = false,updatable = false)
    private String id;

    @NotNull
    @Size(min = SizeMapper.SizeMapperEleve.MIN, max = SizeMapper.SizeMapperEleve.MAX)
    @Column(name = "prenom", length = SizeMapper.SizeMapperEleve.MAX, nullable = false)
    private String prenom;

    @NotNull
    @Size(min = SizeMapper.SizeMapperEleve.MIN, max = SizeMapper.SizeMapperEleve.MAX)
    @Column(name = "nom", length = SizeMapper.SizeMapperEleve.MAX, nullable = false)
    private String nom;

    @Lob
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;
}
