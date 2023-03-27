package com.memoire.kital.raph.web.rest;

import com.memoire.kital.raph.InscriptiondbApp;
import com.memoire.kital.raph.config.TestSecurityConfiguration;
import com.memoire.kital.raph.domain.Inscription;
import com.memoire.kital.raph.domain.Eleve;
import com.memoire.kital.raph.domain.Annee;
import com.memoire.kital.raph.repository.InscriptionRepository;
import com.memoire.kital.raph.service.InscriptionService;
import com.memoire.kital.raph.service.dto.InscriptionDTO;
import com.memoire.kital.raph.service.mapper.InscriptionMapper;
import com.memoire.kital.raph.service.dto.InscriptionCriteria;
import com.memoire.kital.raph.service.InscriptionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.memoire.kital.raph.domain.enumeration.Etat;
/**
 * Integration tests for the {@link InscriptionResource} REST controller.
 */
@SpringBootTest(classes = { InscriptiondbApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class InscriptionResourceIT {

    private static final Instant DEFAULT_DATE_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_CLASSE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TRANSPORT = false;
    private static final Boolean UPDATED_TRANSPORT = true;

    private static final String DEFAULT_GROUPE_TRANSPORT = "AAAAAAAAAA";
    private static final String UPDATED_GROUPE_TRANSPORT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CANTINE = false;
    private static final Boolean UPDATED_CANTINE = true;

    private static final String DEFAULT_GROUPE_CANTINE = "AAAAAAAAAA";
    private static final String UPDATED_GROUPE_CANTINE = "BBBBBBBBBB";

    private static final String DEFAULT_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_PAIEMENT = "BBBBBBBBBB";

    private static final Etat DEFAULT_STATUT = Etat.INSCRIT;
    private static final Etat UPDATED_STATUT = Etat.NON_INSCRIT;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private InscriptionMapper inscriptionMapper;

    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private InscriptionQueryService inscriptionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createEntity(EntityManager em) {
        Inscription inscription = new Inscription();
            inscription.setDateInscription(DEFAULT_DATE_INSCRIPTION);
            inscription.setClasse(DEFAULT_CLASSE);
            inscription.setTransport(DEFAULT_TRANSPORT);
            inscription.setCantine(DEFAULT_CANTINE);
            inscription.setStatut(DEFAULT_STATUT);
        return inscription;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createUpdatedEntity(EntityManager em) {
        Inscription inscription = new Inscription();
        inscription.setDateInscription(UPDATED_DATE_INSCRIPTION);
        inscription.setClasse(UPDATED_CLASSE);
        inscription.setTransport(UPDATED_TRANSPORT);
        inscription.setCantine(UPDATED_CANTINE);
        inscription.setStatut(UPDATED_STATUT);
        return inscription;
    }

    @BeforeEach
    public void initTest() {
        inscription = createEntity(em);
    }

    @Test
    @Transactional
    public void createInscription() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();
        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);
        restInscriptionMockMvc.perform(post("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getDateInscription()).isEqualTo(DEFAULT_DATE_INSCRIPTION);
        assertThat(testInscription.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testInscription.isTransport()).isEqualTo(DEFAULT_TRANSPORT);
        assertThat(testInscription.isCantine()).isEqualTo(DEFAULT_CANTINE);
        assertThat(testInscription.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void createInscriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        // Create the Inscription with an existing ID
        inscription.setId(null);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionMockMvc.perform(post("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateInscriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setDateInscription(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);


        restInscriptionMockMvc.perform(post("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setClasse(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);


        restInscriptionMockMvc.perform(post("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaiementIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);


        restInscriptionMockMvc.perform(post("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setStatut(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);


        restInscriptionMockMvc.perform(post("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInscriptions() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList
        restInscriptionMockMvc.perform(get("/api/inscriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].transport").value(hasItem(DEFAULT_TRANSPORT.booleanValue())))
            .andExpect(jsonPath("$.[*].groupeTransport").value(hasItem(DEFAULT_GROUPE_TRANSPORT)))
            .andExpect(jsonPath("$.[*].cantine").value(hasItem(DEFAULT_CANTINE.booleanValue())))
            .andExpect(jsonPath("$.[*].groupeCantine").value(hasItem(DEFAULT_GROUPE_CANTINE)))
            .andExpect(jsonPath("$.[*].paiement").value(hasItem(DEFAULT_PAIEMENT)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    public void getInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc.perform(get("/api/inscriptions/{id}", inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId()))
            .andExpect(jsonPath("$.dateInscription").value(DEFAULT_DATE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE))
            .andExpect(jsonPath("$.transport").value(DEFAULT_TRANSPORT.booleanValue()))
            .andExpect(jsonPath("$.groupeTransport").value(DEFAULT_GROUPE_TRANSPORT))
            .andExpect(jsonPath("$.cantine").value(DEFAULT_CANTINE.booleanValue()))
            .andExpect(jsonPath("$.groupeCantine").value(DEFAULT_GROUPE_CANTINE))
            .andExpect(jsonPath("$.paiement").value(DEFAULT_PAIEMENT))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }


    @Test
    @Transactional
    public void getInscriptionsByIdFiltering() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        String id = inscription.getId();

        defaultInscriptionShouldBeFound("id.equals=" + id);
        defaultInscriptionShouldNotBeFound("id.notEquals=" + id);

        defaultInscriptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInscriptionShouldNotBeFound("id.greaterThan=" + id);

        defaultInscriptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInscriptionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInscriptionsByDateInscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where dateInscription equals to DEFAULT_DATE_INSCRIPTION
        defaultInscriptionShouldBeFound("dateInscription.equals=" + DEFAULT_DATE_INSCRIPTION);

        // Get all the inscriptionList where dateInscription equals to UPDATED_DATE_INSCRIPTION
        defaultInscriptionShouldNotBeFound("dateInscription.equals=" + UPDATED_DATE_INSCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByDateInscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where dateInscription not equals to DEFAULT_DATE_INSCRIPTION
        defaultInscriptionShouldNotBeFound("dateInscription.notEquals=" + DEFAULT_DATE_INSCRIPTION);

        // Get all the inscriptionList where dateInscription not equals to UPDATED_DATE_INSCRIPTION
        defaultInscriptionShouldBeFound("dateInscription.notEquals=" + UPDATED_DATE_INSCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByDateInscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where dateInscription in DEFAULT_DATE_INSCRIPTION or UPDATED_DATE_INSCRIPTION
        defaultInscriptionShouldBeFound("dateInscription.in=" + DEFAULT_DATE_INSCRIPTION + "," + UPDATED_DATE_INSCRIPTION);

        // Get all the inscriptionList where dateInscription equals to UPDATED_DATE_INSCRIPTION
        defaultInscriptionShouldNotBeFound("dateInscription.in=" + UPDATED_DATE_INSCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByDateInscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where dateInscription is not null
        defaultInscriptionShouldBeFound("dateInscription.specified=true");

        // Get all the inscriptionList where dateInscription is null
        defaultInscriptionShouldNotBeFound("dateInscription.specified=false");
    }

    @Test
    @Transactional
    public void getAllInscriptionsByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where classe equals to DEFAULT_CLASSE
        defaultInscriptionShouldBeFound("classe.equals=" + DEFAULT_CLASSE);

        // Get all the inscriptionList where classe equals to UPDATED_CLASSE
        defaultInscriptionShouldNotBeFound("classe.equals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where classe not equals to DEFAULT_CLASSE
        defaultInscriptionShouldNotBeFound("classe.notEquals=" + DEFAULT_CLASSE);

        // Get all the inscriptionList where classe not equals to UPDATED_CLASSE
        defaultInscriptionShouldBeFound("classe.notEquals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByClasseIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where classe in DEFAULT_CLASSE or UPDATED_CLASSE
        defaultInscriptionShouldBeFound("classe.in=" + DEFAULT_CLASSE + "," + UPDATED_CLASSE);

        // Get all the inscriptionList where classe equals to UPDATED_CLASSE
        defaultInscriptionShouldNotBeFound("classe.in=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where classe is not null
        defaultInscriptionShouldBeFound("classe.specified=true");

        // Get all the inscriptionList where classe is null
        defaultInscriptionShouldNotBeFound("classe.specified=false");
    }
                @Test
    @Transactional
    public void getAllInscriptionsByClasseContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where classe contains DEFAULT_CLASSE
        defaultInscriptionShouldBeFound("classe.contains=" + DEFAULT_CLASSE);

        // Get all the inscriptionList where classe contains UPDATED_CLASSE
        defaultInscriptionShouldNotBeFound("classe.contains=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByClasseNotContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where classe does not contain DEFAULT_CLASSE
        defaultInscriptionShouldNotBeFound("classe.doesNotContain=" + DEFAULT_CLASSE);

        // Get all the inscriptionList where classe does not contain UPDATED_CLASSE
        defaultInscriptionShouldBeFound("classe.doesNotContain=" + UPDATED_CLASSE);
    }


    @Test
    @Transactional
    public void getAllInscriptionsByTransportIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where transport equals to DEFAULT_TRANSPORT
        defaultInscriptionShouldBeFound("transport.equals=" + DEFAULT_TRANSPORT);

        // Get all the inscriptionList where transport equals to UPDATED_TRANSPORT
        defaultInscriptionShouldNotBeFound("transport.equals=" + UPDATED_TRANSPORT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByTransportIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where transport not equals to DEFAULT_TRANSPORT
        defaultInscriptionShouldNotBeFound("transport.notEquals=" + DEFAULT_TRANSPORT);

        // Get all the inscriptionList where transport not equals to UPDATED_TRANSPORT
        defaultInscriptionShouldBeFound("transport.notEquals=" + UPDATED_TRANSPORT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByTransportIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where transport in DEFAULT_TRANSPORT or UPDATED_TRANSPORT
        defaultInscriptionShouldBeFound("transport.in=" + DEFAULT_TRANSPORT + "," + UPDATED_TRANSPORT);

        // Get all the inscriptionList where transport equals to UPDATED_TRANSPORT
        defaultInscriptionShouldNotBeFound("transport.in=" + UPDATED_TRANSPORT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByTransportIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where transport is not null
        defaultInscriptionShouldBeFound("transport.specified=true");

        // Get all the inscriptionList where transport is null
        defaultInscriptionShouldNotBeFound("transport.specified=false");
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeTransportIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeTransport equals to DEFAULT_GROUPE_TRANSPORT
        defaultInscriptionShouldBeFound("groupeTransport.equals=" + DEFAULT_GROUPE_TRANSPORT);

        // Get all the inscriptionList where groupeTransport equals to UPDATED_GROUPE_TRANSPORT
        defaultInscriptionShouldNotBeFound("groupeTransport.equals=" + UPDATED_GROUPE_TRANSPORT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeTransportIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeTransport not equals to DEFAULT_GROUPE_TRANSPORT
        defaultInscriptionShouldNotBeFound("groupeTransport.notEquals=" + DEFAULT_GROUPE_TRANSPORT);

        // Get all the inscriptionList where groupeTransport not equals to UPDATED_GROUPE_TRANSPORT
        defaultInscriptionShouldBeFound("groupeTransport.notEquals=" + UPDATED_GROUPE_TRANSPORT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeTransportIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeTransport in DEFAULT_GROUPE_TRANSPORT or UPDATED_GROUPE_TRANSPORT
        defaultInscriptionShouldBeFound("groupeTransport.in=" + DEFAULT_GROUPE_TRANSPORT + "," + UPDATED_GROUPE_TRANSPORT);

        // Get all the inscriptionList where groupeTransport equals to UPDATED_GROUPE_TRANSPORT
        defaultInscriptionShouldNotBeFound("groupeTransport.in=" + UPDATED_GROUPE_TRANSPORT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeTransportIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeTransport is not null
        defaultInscriptionShouldBeFound("groupeTransport.specified=true");

        // Get all the inscriptionList where groupeTransport is null
        defaultInscriptionShouldNotBeFound("groupeTransport.specified=false");
    }
                @Test
    @Transactional
    public void getAllInscriptionsByGroupeTransportContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeTransport contains DEFAULT_GROUPE_TRANSPORT
        defaultInscriptionShouldBeFound("groupeTransport.contains=" + DEFAULT_GROUPE_TRANSPORT);

        // Get all the inscriptionList where groupeTransport contains UPDATED_GROUPE_TRANSPORT
        defaultInscriptionShouldNotBeFound("groupeTransport.contains=" + UPDATED_GROUPE_TRANSPORT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeTransportNotContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeTransport does not contain DEFAULT_GROUPE_TRANSPORT
        defaultInscriptionShouldNotBeFound("groupeTransport.doesNotContain=" + DEFAULT_GROUPE_TRANSPORT);

        // Get all the inscriptionList where groupeTransport does not contain UPDATED_GROUPE_TRANSPORT
        defaultInscriptionShouldBeFound("groupeTransport.doesNotContain=" + UPDATED_GROUPE_TRANSPORT);
    }


    @Test
    @Transactional
    public void getAllInscriptionsByCantineIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where cantine equals to DEFAULT_CANTINE
        defaultInscriptionShouldBeFound("cantine.equals=" + DEFAULT_CANTINE);

        // Get all the inscriptionList where cantine equals to UPDATED_CANTINE
        defaultInscriptionShouldNotBeFound("cantine.equals=" + UPDATED_CANTINE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByCantineIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where cantine not equals to DEFAULT_CANTINE
        defaultInscriptionShouldNotBeFound("cantine.notEquals=" + DEFAULT_CANTINE);

        // Get all the inscriptionList where cantine not equals to UPDATED_CANTINE
        defaultInscriptionShouldBeFound("cantine.notEquals=" + UPDATED_CANTINE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByCantineIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where cantine in DEFAULT_CANTINE or UPDATED_CANTINE
        defaultInscriptionShouldBeFound("cantine.in=" + DEFAULT_CANTINE + "," + UPDATED_CANTINE);

        // Get all the inscriptionList where cantine equals to UPDATED_CANTINE
        defaultInscriptionShouldNotBeFound("cantine.in=" + UPDATED_CANTINE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByCantineIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where cantine is not null
        defaultInscriptionShouldBeFound("cantine.specified=true");

        // Get all the inscriptionList where cantine is null
        defaultInscriptionShouldNotBeFound("cantine.specified=false");
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeCantineIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeCantine equals to DEFAULT_GROUPE_CANTINE
        defaultInscriptionShouldBeFound("groupeCantine.equals=" + DEFAULT_GROUPE_CANTINE);

        // Get all the inscriptionList where groupeCantine equals to UPDATED_GROUPE_CANTINE
        defaultInscriptionShouldNotBeFound("groupeCantine.equals=" + UPDATED_GROUPE_CANTINE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeCantineIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeCantine not equals to DEFAULT_GROUPE_CANTINE
        defaultInscriptionShouldNotBeFound("groupeCantine.notEquals=" + DEFAULT_GROUPE_CANTINE);

        // Get all the inscriptionList where groupeCantine not equals to UPDATED_GROUPE_CANTINE
        defaultInscriptionShouldBeFound("groupeCantine.notEquals=" + UPDATED_GROUPE_CANTINE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeCantineIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeCantine in DEFAULT_GROUPE_CANTINE or UPDATED_GROUPE_CANTINE
        defaultInscriptionShouldBeFound("groupeCantine.in=" + DEFAULT_GROUPE_CANTINE + "," + UPDATED_GROUPE_CANTINE);

        // Get all the inscriptionList where groupeCantine equals to UPDATED_GROUPE_CANTINE
        defaultInscriptionShouldNotBeFound("groupeCantine.in=" + UPDATED_GROUPE_CANTINE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeCantineIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeCantine is not null
        defaultInscriptionShouldBeFound("groupeCantine.specified=true");

        // Get all the inscriptionList where groupeCantine is null
        defaultInscriptionShouldNotBeFound("groupeCantine.specified=false");
    }
                @Test
    @Transactional
    public void getAllInscriptionsByGroupeCantineContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeCantine contains DEFAULT_GROUPE_CANTINE
        defaultInscriptionShouldBeFound("groupeCantine.contains=" + DEFAULT_GROUPE_CANTINE);

        // Get all the inscriptionList where groupeCantine contains UPDATED_GROUPE_CANTINE
        defaultInscriptionShouldNotBeFound("groupeCantine.contains=" + UPDATED_GROUPE_CANTINE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByGroupeCantineNotContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where groupeCantine does not contain DEFAULT_GROUPE_CANTINE
        defaultInscriptionShouldNotBeFound("groupeCantine.doesNotContain=" + DEFAULT_GROUPE_CANTINE);

        // Get all the inscriptionList where groupeCantine does not contain UPDATED_GROUPE_CANTINE
        defaultInscriptionShouldBeFound("groupeCantine.doesNotContain=" + UPDATED_GROUPE_CANTINE);
    }


    @Test
    @Transactional
    public void getAllInscriptionsByPaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where paiement equals to DEFAULT_PAIEMENT
        defaultInscriptionShouldBeFound("paiement.equals=" + DEFAULT_PAIEMENT);

        // Get all the inscriptionList where paiement equals to UPDATED_PAIEMENT
        defaultInscriptionShouldNotBeFound("paiement.equals=" + UPDATED_PAIEMENT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByPaiementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where paiement not equals to DEFAULT_PAIEMENT
        defaultInscriptionShouldNotBeFound("paiement.notEquals=" + DEFAULT_PAIEMENT);

        // Get all the inscriptionList where paiement not equals to UPDATED_PAIEMENT
        defaultInscriptionShouldBeFound("paiement.notEquals=" + UPDATED_PAIEMENT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByPaiementIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where paiement in DEFAULT_PAIEMENT or UPDATED_PAIEMENT
        defaultInscriptionShouldBeFound("paiement.in=" + DEFAULT_PAIEMENT + "," + UPDATED_PAIEMENT);

        // Get all the inscriptionList where paiement equals to UPDATED_PAIEMENT
        defaultInscriptionShouldNotBeFound("paiement.in=" + UPDATED_PAIEMENT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByPaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where paiement is not null
        defaultInscriptionShouldBeFound("paiement.specified=true");

        // Get all the inscriptionList where paiement is null
        defaultInscriptionShouldNotBeFound("paiement.specified=false");
    }
                @Test
    @Transactional
    public void getAllInscriptionsByPaiementContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where paiement contains DEFAULT_PAIEMENT
        defaultInscriptionShouldBeFound("paiement.contains=" + DEFAULT_PAIEMENT);

        // Get all the inscriptionList where paiement contains UPDATED_PAIEMENT
        defaultInscriptionShouldNotBeFound("paiement.contains=" + UPDATED_PAIEMENT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByPaiementNotContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where paiement does not contain DEFAULT_PAIEMENT
        defaultInscriptionShouldNotBeFound("paiement.doesNotContain=" + DEFAULT_PAIEMENT);

        // Get all the inscriptionList where paiement does not contain UPDATED_PAIEMENT
        defaultInscriptionShouldBeFound("paiement.doesNotContain=" + UPDATED_PAIEMENT);
    }


    @Test
    @Transactional
    public void getAllInscriptionsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where statut equals to DEFAULT_STATUT
        defaultInscriptionShouldBeFound("statut.equals=" + DEFAULT_STATUT);

        // Get all the inscriptionList where statut equals to UPDATED_STATUT
        defaultInscriptionShouldNotBeFound("statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByStatutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where statut not equals to DEFAULT_STATUT
        defaultInscriptionShouldNotBeFound("statut.notEquals=" + DEFAULT_STATUT);

        // Get all the inscriptionList where statut not equals to UPDATED_STATUT
        defaultInscriptionShouldBeFound("statut.notEquals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where statut in DEFAULT_STATUT or UPDATED_STATUT
        defaultInscriptionShouldBeFound("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT);

        // Get all the inscriptionList where statut equals to UPDATED_STATUT
        defaultInscriptionShouldNotBeFound("statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where statut is not null
        defaultInscriptionShouldBeFound("statut.specified=true");

        // Get all the inscriptionList where statut is null
        defaultInscriptionShouldNotBeFound("statut.specified=false");
    }

    @Test
    @Transactional
    public void getAllInscriptionsByEleveIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);
        Eleve eleve = EleveResourceIT.createEntity(em);
        em.persist(eleve);
        em.flush();
        inscription.setEleve(eleve);
        inscriptionRepository.saveAndFlush(inscription);
        String eleveId = eleve.getId();

        // Get all the inscriptionList where eleve equals to eleveId
        defaultInscriptionShouldBeFound("eleveId.equals=" + eleveId);

        // Get all the inscriptionList where eleve equals to eleveId + 1
        defaultInscriptionShouldNotBeFound("eleveId.equals=" + (eleveId + 1));
    }


    @Test
    @Transactional
    public void getAllInscriptionsByAnneeIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);
        Annee annee = AnneeResourceIT.createEntity(em);
        em.persist(annee);
        em.flush();
        inscription.setAnnee(annee);
        inscriptionRepository.saveAndFlush(inscription);
        String anneeId = annee.getId();

        // Get all the inscriptionList where annee equals to anneeId
        defaultInscriptionShouldBeFound("anneeId.equals=" + anneeId);

        // Get all the inscriptionList where annee equals to anneeId + 1
        defaultInscriptionShouldNotBeFound("anneeId.equals=" + (anneeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInscriptionShouldBeFound(String filter) throws Exception {
        restInscriptionMockMvc.perform(get("/api/inscriptions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].transport").value(hasItem(DEFAULT_TRANSPORT.booleanValue())))
            .andExpect(jsonPath("$.[*].groupeTransport").value(hasItem(DEFAULT_GROUPE_TRANSPORT)))
            .andExpect(jsonPath("$.[*].cantine").value(hasItem(DEFAULT_CANTINE.booleanValue())))
            .andExpect(jsonPath("$.[*].groupeCantine").value(hasItem(DEFAULT_GROUPE_CANTINE)))
            .andExpect(jsonPath("$.[*].paiement").value(hasItem(DEFAULT_PAIEMENT)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));

        // Check, that the count call also returns 1
        restInscriptionMockMvc.perform(get("/api/inscriptions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInscriptionShouldNotBeFound(String filter) throws Exception {
        restInscriptionMockMvc.perform(get("/api/inscriptions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInscriptionMockMvc.perform(get("/api/inscriptions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get("/api/inscriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription
        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).get();
        // Disconnect from session so that the updates on updatedInscription are not directly saved in db
        em.detach(updatedInscription);

        updatedInscription.setDateInscription(UPDATED_DATE_INSCRIPTION);
        updatedInscription.setClasse(UPDATED_CLASSE);
        updatedInscription.setTransport(UPDATED_TRANSPORT);
        updatedInscription.setCantine(UPDATED_CANTINE);
        updatedInscription.setStatut(UPDATED_STATUT);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(updatedInscription);

        restInscriptionMockMvc.perform(put("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);
        assertThat(testInscription.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testInscription.isTransport()).isEqualTo(UPDATED_TRANSPORT);
        assertThat(testInscription.isCantine()).isEqualTo(UPDATED_CANTINE);
        assertThat(testInscription.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void updateNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc.perform(put("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeDelete = inscriptionRepository.findAll().size();

        // Delete the inscription
        restInscriptionMockMvc.perform(delete("/api/inscriptions/{id}", inscription.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
