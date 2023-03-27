package com.memoire.kital.raph.web.rest;

import com.memoire.kital.raph.InscriptiondbApp;
import com.memoire.kital.raph.config.TestSecurityConfiguration;
import com.memoire.kital.raph.domain.Annee;
import com.memoire.kital.raph.repository.AnneeRepository;
import com.memoire.kital.raph.service.AnneeService;
import com.memoire.kital.raph.service.dto.AnneeDTO;
import com.memoire.kital.raph.service.mapper.AnneeMapper;
import com.memoire.kital.raph.service.dto.AnneeCriteria;
import com.memoire.kital.raph.service.AnneeQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AnneeResource} REST controller.
 */
@SpringBootTest(classes = { InscriptiondbApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AnneeResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    @Autowired
    private AnneeRepository anneeRepository;

    @Autowired
    private AnneeMapper anneeMapper;

    @Autowired
    private AnneeService anneeService;

    @Autowired
    private AnneeQueryService anneeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnneeMockMvc;

    private Annee annee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annee createEntity(EntityManager em) {
        Annee annee = new Annee()
            .nom(DEFAULT_NOM)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        return annee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annee createUpdatedEntity(EntityManager em) {
        Annee annee = new Annee()
            .nom(UPDATED_NOM)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        return annee;
    }

    @BeforeEach
    public void initTest() {
        annee = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnnee() throws Exception {
        int databaseSizeBeforeCreate = anneeRepository.findAll().size();
        // Create the Annee
        AnneeDTO anneeDTO = anneeMapper.toDto(annee);
        restAnneeMockMvc.perform(post("/api/annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeDTO)))
            .andExpect(status().isCreated());

        // Validate the Annee in the database
        List<Annee> anneeList = anneeRepository.findAll();
        assertThat(anneeList).hasSize(databaseSizeBeforeCreate + 1);
        Annee testAnnee = anneeList.get(anneeList.size() - 1);
        assertThat(testAnnee.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAnnee.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAnnee.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    public void createAnneeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = anneeRepository.findAll().size();

        // Create the Annee with an existing ID
        annee.setId(null);
        AnneeDTO anneeDTO = anneeMapper.toDto(annee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnneeMockMvc.perform(post("/api/annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Annee in the database
        List<Annee> anneeList = anneeRepository.findAll();
        assertThat(anneeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeRepository.findAll().size();
        // set the field null
        annee.setNom(null);

        // Create the Annee, which fails.
        AnneeDTO anneeDTO = anneeMapper.toDto(annee);


        restAnneeMockMvc.perform(post("/api/annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeDTO)))
            .andExpect(status().isBadRequest());

        List<Annee> anneeList = anneeRepository.findAll();
        assertThat(anneeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeRepository.findAll().size();
        // set the field null
        annee.setDateDebut(null);

        // Create the Annee, which fails.
        AnneeDTO anneeDTO = anneeMapper.toDto(annee);


        restAnneeMockMvc.perform(post("/api/annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeDTO)))
            .andExpect(status().isBadRequest());

        List<Annee> anneeList = anneeRepository.findAll();
        assertThat(anneeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeRepository.findAll().size();
        // set the field null
        annee.setDateFin(null);

        // Create the Annee, which fails.
        AnneeDTO anneeDTO = anneeMapper.toDto(annee);


        restAnneeMockMvc.perform(post("/api/annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeDTO)))
            .andExpect(status().isBadRequest());

        List<Annee> anneeList = anneeRepository.findAll();
        assertThat(anneeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnnees() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList
        restAnneeMockMvc.perform(get("/api/annees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annee.getId())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    public void getAnnee() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get the annee
        restAnneeMockMvc.perform(get("/api/annees/{id}", annee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annee.getId()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }


    @Test
    @Transactional
    public void getAnneesByIdFiltering() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        String id = annee.getId();

        defaultAnneeShouldBeFound("id.equals=" + id);
        defaultAnneeShouldNotBeFound("id.notEquals=" + id);

        defaultAnneeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnneeShouldNotBeFound("id.greaterThan=" + id);

        defaultAnneeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnneeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnneesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where nom equals to DEFAULT_NOM
        defaultAnneeShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the anneeList where nom equals to UPDATED_NOM
        defaultAnneeShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllAnneesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where nom not equals to DEFAULT_NOM
        defaultAnneeShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the anneeList where nom not equals to UPDATED_NOM
        defaultAnneeShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllAnneesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultAnneeShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the anneeList where nom equals to UPDATED_NOM
        defaultAnneeShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllAnneesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where nom is not null
        defaultAnneeShouldBeFound("nom.specified=true");

        // Get all the anneeList where nom is null
        defaultAnneeShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnneesByNomContainsSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where nom contains DEFAULT_NOM
        defaultAnneeShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the anneeList where nom contains UPDATED_NOM
        defaultAnneeShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllAnneesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where nom does not contain DEFAULT_NOM
        defaultAnneeShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the anneeList where nom does not contain UPDATED_NOM
        defaultAnneeShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllAnneesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultAnneeShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the anneeList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultAnneeShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateDebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateDebut not equals to DEFAULT_DATE_DEBUT
        defaultAnneeShouldNotBeFound("dateDebut.notEquals=" + DEFAULT_DATE_DEBUT);

        // Get all the anneeList where dateDebut not equals to UPDATED_DATE_DEBUT
        defaultAnneeShouldBeFound("dateDebut.notEquals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultAnneeShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the anneeList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultAnneeShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateDebut is not null
        defaultAnneeShouldBeFound("dateDebut.specified=true");

        // Get all the anneeList where dateDebut is null
        defaultAnneeShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnneesByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateDebut is greater than or equal to DEFAULT_DATE_DEBUT
        defaultAnneeShouldBeFound("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the anneeList where dateDebut is greater than or equal to UPDATED_DATE_DEBUT
        defaultAnneeShouldNotBeFound("dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateDebut is less than or equal to DEFAULT_DATE_DEBUT
        defaultAnneeShouldBeFound("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the anneeList where dateDebut is less than or equal to SMALLER_DATE_DEBUT
        defaultAnneeShouldNotBeFound("dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateDebut is less than DEFAULT_DATE_DEBUT
        defaultAnneeShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the anneeList where dateDebut is less than UPDATED_DATE_DEBUT
        defaultAnneeShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateDebut is greater than DEFAULT_DATE_DEBUT
        defaultAnneeShouldNotBeFound("dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);

        // Get all the anneeList where dateDebut is greater than SMALLER_DATE_DEBUT
        defaultAnneeShouldBeFound("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT);
    }


    @Test
    @Transactional
    public void getAllAnneesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateFin equals to DEFAULT_DATE_FIN
        defaultAnneeShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the anneeList where dateFin equals to UPDATED_DATE_FIN
        defaultAnneeShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateFin not equals to DEFAULT_DATE_FIN
        defaultAnneeShouldNotBeFound("dateFin.notEquals=" + DEFAULT_DATE_FIN);

        // Get all the anneeList where dateFin not equals to UPDATED_DATE_FIN
        defaultAnneeShouldBeFound("dateFin.notEquals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultAnneeShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the anneeList where dateFin equals to UPDATED_DATE_FIN
        defaultAnneeShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateFin is not null
        defaultAnneeShouldBeFound("dateFin.specified=true");

        // Get all the anneeList where dateFin is null
        defaultAnneeShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnneesByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateFin is greater than or equal to DEFAULT_DATE_FIN
        defaultAnneeShouldBeFound("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the anneeList where dateFin is greater than or equal to UPDATED_DATE_FIN
        defaultAnneeShouldNotBeFound("dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateFin is less than or equal to DEFAULT_DATE_FIN
        defaultAnneeShouldBeFound("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the anneeList where dateFin is less than or equal to SMALLER_DATE_FIN
        defaultAnneeShouldNotBeFound("dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateFin is less than DEFAULT_DATE_FIN
        defaultAnneeShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the anneeList where dateFin is less than UPDATED_DATE_FIN
        defaultAnneeShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllAnneesByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        // Get all the anneeList where dateFin is greater than DEFAULT_DATE_FIN
        defaultAnneeShouldNotBeFound("dateFin.greaterThan=" + DEFAULT_DATE_FIN);

        // Get all the anneeList where dateFin is greater than SMALLER_DATE_FIN
        defaultAnneeShouldBeFound("dateFin.greaterThan=" + SMALLER_DATE_FIN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnneeShouldBeFound(String filter) throws Exception {
        restAnneeMockMvc.perform(get("/api/annees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annee.getId())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));

        // Check, that the count call also returns 1
        restAnneeMockMvc.perform(get("/api/annees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnneeShouldNotBeFound(String filter) throws Exception {
        restAnneeMockMvc.perform(get("/api/annees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnneeMockMvc.perform(get("/api/annees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAnnee() throws Exception {
        // Get the annee
        restAnneeMockMvc.perform(get("/api/annees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnee() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        int databaseSizeBeforeUpdate = anneeRepository.findAll().size();

        // Update the annee
        Annee updatedAnnee = anneeRepository.findById(annee.getId()).get();
        // Disconnect from session so that the updates on updatedAnnee are not directly saved in db
        em.detach(updatedAnnee);
        updatedAnnee
            .nom(UPDATED_NOM)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        AnneeDTO anneeDTO = anneeMapper.toDto(updatedAnnee);

        restAnneeMockMvc.perform(put("/api/annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeDTO)))
            .andExpect(status().isOk());

        // Validate the Annee in the database
        List<Annee> anneeList = anneeRepository.findAll();
        assertThat(anneeList).hasSize(databaseSizeBeforeUpdate);
        Annee testAnnee = anneeList.get(anneeList.size() - 1);
        assertThat(testAnnee.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAnnee.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAnnee.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnee() throws Exception {
        int databaseSizeBeforeUpdate = anneeRepository.findAll().size();

        // Create the Annee
        AnneeDTO anneeDTO = anneeMapper.toDto(annee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeMockMvc.perform(put("/api/annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Annee in the database
        List<Annee> anneeList = anneeRepository.findAll();
        assertThat(anneeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnnee() throws Exception {
        // Initialize the database
        anneeRepository.saveAndFlush(annee);

        int databaseSizeBeforeDelete = anneeRepository.findAll().size();

        // Delete the annee
        restAnneeMockMvc.perform(delete("/api/annees/{id}", annee.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Annee> anneeList = anneeRepository.findAll();
        assertThat(anneeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
