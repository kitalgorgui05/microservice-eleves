package com.memoire.kital.raph.web.rest;

import com.memoire.kital.raph.InscriptiondbApp;
import com.memoire.kital.raph.config.TestSecurityConfiguration;
import com.memoire.kital.raph.domain.Tuteur;
import com.memoire.kital.raph.repository.TuteurRepository;
import com.memoire.kital.raph.service.TuteurService;
import com.memoire.kital.raph.service.dto.TuteurDTO;
import com.memoire.kital.raph.service.mapper.TuteurMapper;
import com.memoire.kital.raph.service.dto.TuteurCriteria;
import com.memoire.kital.raph.service.TuteurQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.memoire.kital.raph.domain.enumeration.Sexe;
/**
 * Integration tests for the {@link TuteurResource} REST controller.
 */
@SpringBootTest(classes = { InscriptiondbApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class TuteurResourceIT {

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.MASCULIN;
    private static final Sexe UPDATED_SEXE = Sexe.FEMININ;

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private TuteurMapper tuteurMapper;

    @Autowired
    private TuteurService tuteurService;

    @Autowired
    private TuteurQueryService tuteurQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTuteurMockMvc;

    private Tuteur tuteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tuteur createEntity(EntityManager em) {
        Tuteur tuteur = new Tuteur()
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .adresse(DEFAULT_ADRESSE)
            .email(DEFAULT_EMAIL)
            .sexe(DEFAULT_SEXE);
        return tuteur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tuteur createUpdatedEntity(EntityManager em) {
        Tuteur tuteur = new Tuteur()
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .sexe(UPDATED_SEXE);
        return tuteur;
    }

    @BeforeEach
    public void initTest() {
        tuteur = createEntity(em);
    }

    @Test
    @Transactional
    public void createTuteur() throws Exception {
        int databaseSizeBeforeCreate = tuteurRepository.findAll().size();
        // Create the Tuteur
        TuteurDTO tuteurDTO = tuteurMapper.toDto(tuteur);
        restTuteurMockMvc.perform(post("/api/tuteurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tuteurDTO)))
            .andExpect(status().isCreated());

        // Validate the Tuteur in the database
        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeCreate + 1);
        Tuteur testTuteur = tuteurList.get(tuteurList.size() - 1);
        assertThat(testTuteur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testTuteur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testTuteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testTuteur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTuteur.getSexe()).isEqualTo(DEFAULT_SEXE);
    }

    @Test
    @Transactional
    public void createTuteurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tuteurRepository.findAll().size();

        // Create the Tuteur with an existing ID
        tuteur.setId(null);
        TuteurDTO tuteurDTO = tuteurMapper.toDto(tuteur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTuteurMockMvc.perform(post("/api/tuteurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tuteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tuteur in the database
        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = tuteurRepository.findAll().size();
        // set the field null
        tuteur.setPrenom(null);

        // Create the Tuteur, which fails.
        TuteurDTO tuteurDTO = tuteurMapper.toDto(tuteur);


        restTuteurMockMvc.perform(post("/api/tuteurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tuteurDTO)))
            .andExpect(status().isBadRequest());

        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = tuteurRepository.findAll().size();
        // set the field null
        tuteur.setNom(null);

        // Create the Tuteur, which fails.
        TuteurDTO tuteurDTO = tuteurMapper.toDto(tuteur);


        restTuteurMockMvc.perform(post("/api/tuteurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tuteurDTO)))
            .andExpect(status().isBadRequest());

        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = tuteurRepository.findAll().size();
        // set the field null
        tuteur.setEmail(null);

        // Create the Tuteur, which fails.
        TuteurDTO tuteurDTO = tuteurMapper.toDto(tuteur);


        restTuteurMockMvc.perform(post("/api/tuteurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tuteurDTO)))
            .andExpect(status().isBadRequest());

        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tuteurRepository.findAll().size();
        // set the field null
        tuteur.setSexe(null);

        // Create the Tuteur, which fails.
        TuteurDTO tuteurDTO = tuteurMapper.toDto(tuteur);


        restTuteurMockMvc.perform(post("/api/tuteurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tuteurDTO)))
            .andExpect(status().isBadRequest());

        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTuteurs() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList
        restTuteurMockMvc.perform(get("/api/tuteurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tuteur.getId())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));
    }

    @Test
    @Transactional
    public void getTuteur() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get the tuteur
        restTuteurMockMvc.perform(get("/api/tuteurs/{id}", tuteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tuteur.getId()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()));
    }


    @Test
    @Transactional
    public void getTuteursByIdFiltering() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        String id = tuteur.getId();

        defaultTuteurShouldBeFound("id.equals=" + id);
        defaultTuteurShouldNotBeFound("id.notEquals=" + id);

        defaultTuteurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTuteurShouldNotBeFound("id.greaterThan=" + id);

        defaultTuteurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTuteurShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTuteursByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where prenom equals to DEFAULT_PRENOM
        defaultTuteurShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the tuteurList where prenom equals to UPDATED_PRENOM
        defaultTuteurShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllTuteursByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where prenom not equals to DEFAULT_PRENOM
        defaultTuteurShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the tuteurList where prenom not equals to UPDATED_PRENOM
        defaultTuteurShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllTuteursByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultTuteurShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the tuteurList where prenom equals to UPDATED_PRENOM
        defaultTuteurShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllTuteursByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where prenom is not null
        defaultTuteurShouldBeFound("prenom.specified=true");

        // Get all the tuteurList where prenom is null
        defaultTuteurShouldNotBeFound("prenom.specified=false");
    }
                @Test
    @Transactional
    public void getAllTuteursByPrenomContainsSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where prenom contains DEFAULT_PRENOM
        defaultTuteurShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the tuteurList where prenom contains UPDATED_PRENOM
        defaultTuteurShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllTuteursByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where prenom does not contain DEFAULT_PRENOM
        defaultTuteurShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the tuteurList where prenom does not contain UPDATED_PRENOM
        defaultTuteurShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }


    @Test
    @Transactional
    public void getAllTuteursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where nom equals to DEFAULT_NOM
        defaultTuteurShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the tuteurList where nom equals to UPDATED_NOM
        defaultTuteurShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllTuteursByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where nom not equals to DEFAULT_NOM
        defaultTuteurShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the tuteurList where nom not equals to UPDATED_NOM
        defaultTuteurShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllTuteursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultTuteurShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the tuteurList where nom equals to UPDATED_NOM
        defaultTuteurShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllTuteursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where nom is not null
        defaultTuteurShouldBeFound("nom.specified=true");

        // Get all the tuteurList where nom is null
        defaultTuteurShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllTuteursByNomContainsSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where nom contains DEFAULT_NOM
        defaultTuteurShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the tuteurList where nom contains UPDATED_NOM
        defaultTuteurShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllTuteursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where nom does not contain DEFAULT_NOM
        defaultTuteurShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the tuteurList where nom does not contain UPDATED_NOM
        defaultTuteurShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllTuteursByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where email equals to DEFAULT_EMAIL
        defaultTuteurShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the tuteurList where email equals to UPDATED_EMAIL
        defaultTuteurShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTuteursByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where email not equals to DEFAULT_EMAIL
        defaultTuteurShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the tuteurList where email not equals to UPDATED_EMAIL
        defaultTuteurShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTuteursByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTuteurShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the tuteurList where email equals to UPDATED_EMAIL
        defaultTuteurShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTuteursByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where email is not null
        defaultTuteurShouldBeFound("email.specified=true");

        // Get all the tuteurList where email is null
        defaultTuteurShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllTuteursByEmailContainsSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where email contains DEFAULT_EMAIL
        defaultTuteurShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the tuteurList where email contains UPDATED_EMAIL
        defaultTuteurShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTuteursByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where email does not contain DEFAULT_EMAIL
        defaultTuteurShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the tuteurList where email does not contain UPDATED_EMAIL
        defaultTuteurShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllTuteursBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where sexe equals to DEFAULT_SEXE
        defaultTuteurShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the tuteurList where sexe equals to UPDATED_SEXE
        defaultTuteurShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllTuteursBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where sexe not equals to DEFAULT_SEXE
        defaultTuteurShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the tuteurList where sexe not equals to UPDATED_SEXE
        defaultTuteurShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllTuteursBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultTuteurShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the tuteurList where sexe equals to UPDATED_SEXE
        defaultTuteurShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllTuteursBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        // Get all the tuteurList where sexe is not null
        defaultTuteurShouldBeFound("sexe.specified=true");

        // Get all the tuteurList where sexe is null
        defaultTuteurShouldNotBeFound("sexe.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTuteurShouldBeFound(String filter) throws Exception {
        restTuteurMockMvc.perform(get("/api/tuteurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tuteur.getId())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));

        // Check, that the count call also returns 1
        restTuteurMockMvc.perform(get("/api/tuteurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTuteurShouldNotBeFound(String filter) throws Exception {
        restTuteurMockMvc.perform(get("/api/tuteurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTuteurMockMvc.perform(get("/api/tuteurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTuteur() throws Exception {
        // Get the tuteur
        restTuteurMockMvc.perform(get("/api/tuteurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTuteur() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        int databaseSizeBeforeUpdate = tuteurRepository.findAll().size();

        // Update the tuteur
        Tuteur updatedTuteur = tuteurRepository.findById(tuteur.getId()).get();
        // Disconnect from session so that the updates on updatedTuteur are not directly saved in db
        em.detach(updatedTuteur);
        updatedTuteur
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .sexe(UPDATED_SEXE);
        TuteurDTO tuteurDTO = tuteurMapper.toDto(updatedTuteur);

        restTuteurMockMvc.perform(put("/api/tuteurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tuteurDTO)))
            .andExpect(status().isOk());

        // Validate the Tuteur in the database
        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeUpdate);
        Tuteur testTuteur = tuteurList.get(tuteurList.size() - 1);
        assertThat(testTuteur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testTuteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testTuteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testTuteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTuteur.getSexe()).isEqualTo(UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void updateNonExistingTuteur() throws Exception {
        int databaseSizeBeforeUpdate = tuteurRepository.findAll().size();

        // Create the Tuteur
        TuteurDTO tuteurDTO = tuteurMapper.toDto(tuteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTuteurMockMvc.perform(put("/api/tuteurs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tuteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tuteur in the database
        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTuteur() throws Exception {
        // Initialize the database
        tuteurRepository.saveAndFlush(tuteur);

        int databaseSizeBeforeDelete = tuteurRepository.findAll().size();

        // Delete the tuteur
        restTuteurMockMvc.perform(delete("/api/tuteurs/{id}", tuteur.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tuteur> tuteurList = tuteurRepository.findAll();
        assertThat(tuteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
