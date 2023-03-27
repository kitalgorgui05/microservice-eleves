package com.memoire.kital.raph.web.rest;

import com.memoire.kital.raph.InscriptiondbApp;
import com.memoire.kital.raph.config.TestSecurityConfiguration;
import com.memoire.kital.raph.domain.Eleve;
import com.memoire.kital.raph.domain.Tuteur;
import com.memoire.kital.raph.repository.EleveRepository;
import com.memoire.kital.raph.service.EleveService;
import com.memoire.kital.raph.service.dto.EleveDTO;
import com.memoire.kital.raph.service.mapper.EleveMapper;
import com.memoire.kital.raph.service.dto.EleveCriteria;
import com.memoire.kital.raph.service.EleveQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.memoire.kital.raph.domain.enumeration.Sexe;
/**
 * Integration tests for the {@link EleveResource} REST controller.
 */
@SpringBootTest(classes = { InscriptiondbApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class EleveResourceIT {

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.MASCULIN;
    private static final Sexe UPDATED_SEXE = Sexe.FEMININ;

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBBBBBBB";

    @Autowired
    private EleveRepository eleveRepository;

    @Autowired
    private EleveMapper eleveMapper;

    @Autowired
    private EleveService eleveService;

    @Autowired
    private EleveQueryService eleveQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEleveMockMvc;

    private Eleve eleve;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eleve createEntity(EntityManager em) {
        Eleve eleve = new Eleve();
            eleve.setPhoto(DEFAULT_PHOTO);
            eleve.setPhotoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
            eleve.setMatricule(DEFAULT_MATRICULE);
            eleve.setPrenom(DEFAULT_PRENOM);
            eleve.setNom(DEFAULT_NOM);
            eleve.setSexe(DEFAULT_SEXE);
            eleve.setAdresse(DEFAULT_ADRESSE);
            eleve.setTelephone(DEFAULT_TELEPHONE);
            eleve.setEmail(DEFAULT_EMAIL);
            eleve.setDateNaissance(DEFAULT_DATE_NAISSANCE);
            eleve.setLieuNaissance(DEFAULT_LIEU_NAISSANCE);
        return eleve;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eleve createUpdatedEntity(EntityManager em) {
        Eleve eleve = new Eleve();
            eleve.setPhoto(UPDATED_PHOTO);
            eleve.setPhotoContentType(UPDATED_PHOTO_CONTENT_TYPE);
            eleve.setMatricule(UPDATED_MATRICULE);
            eleve.setPrenom(UPDATED_PRENOM);
            eleve.setNom(UPDATED_NOM);
            eleve.setSexe(UPDATED_SEXE);
            eleve.setAdresse(UPDATED_ADRESSE);
            eleve.setTelephone(UPDATED_TELEPHONE);
            eleve.setEmail(UPDATED_EMAIL);
            eleve.setDateNaissance(UPDATED_DATE_NAISSANCE);
            eleve.setLieuNaissance(UPDATED_LIEU_NAISSANCE);
        return eleve;
    }

    @BeforeEach
    public void initTest() {
        eleve = createEntity(em);
    }

    @Test
    @Transactional
    public void createEleve() throws Exception {
        int databaseSizeBeforeCreate = eleveRepository.findAll().size();
        // Create the Eleve
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);
        restEleveMockMvc.perform(post("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isCreated());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeCreate + 1);
        Eleve testEleve = eleveList.get(eleveList.size() - 1);
        assertThat(testEleve.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEleve.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testEleve.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testEleve.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEleve.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEleve.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEleve.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testEleve.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testEleve.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEleve.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testEleve.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void createEleveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eleveRepository.findAll().size();

        // Create the Eleve with an existing ID
        eleve.setId(null);
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEleveMockMvc.perform(post("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMatriculeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setMatricule(null);

        // Create the Eleve, which fails.
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);


        restEleveMockMvc.perform(post("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setPrenom(null);

        // Create the Eleve, which fails.
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);


        restEleveMockMvc.perform(post("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setNom(null);

        // Create the Eleve, which fails.
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);


        restEleveMockMvc.perform(post("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setSexe(null);

        // Create the Eleve, which fails.
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);


        restEleveMockMvc.perform(post("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setDateNaissance(null);

        // Create the Eleve, which fails.
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);


        restEleveMockMvc.perform(post("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLieuNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setLieuNaissance(null);

        // Create the Eleve, which fails.
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);


        restEleveMockMvc.perform(post("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEleves() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList
        restEleveMockMvc.perform(get("/api/eleves?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eleve.getId())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)));
    }

    @Test
    @Transactional
    public void getEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get the eleve
        restEleveMockMvc.perform(get("/api/eleves/{id}", eleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eleve.getId()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE));
    }


    @Test
    @Transactional
    public void getElevesByIdFiltering() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        String id = eleve.getId();

        defaultEleveShouldBeFound("id.equals=" + id);
        defaultEleveShouldNotBeFound("id.notEquals=" + id);

        defaultEleveShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEleveShouldNotBeFound("id.greaterThan=" + id);

        defaultEleveShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEleveShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllElevesByMatriculeIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where matricule equals to DEFAULT_MATRICULE
        defaultEleveShouldBeFound("matricule.equals=" + DEFAULT_MATRICULE);

        // Get all the eleveList where matricule equals to UPDATED_MATRICULE
        defaultEleveShouldNotBeFound("matricule.equals=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    public void getAllElevesByMatriculeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where matricule not equals to DEFAULT_MATRICULE
        defaultEleveShouldNotBeFound("matricule.notEquals=" + DEFAULT_MATRICULE);

        // Get all the eleveList where matricule not equals to UPDATED_MATRICULE
        defaultEleveShouldBeFound("matricule.notEquals=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    public void getAllElevesByMatriculeIsInShouldWork() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where matricule in DEFAULT_MATRICULE or UPDATED_MATRICULE
        defaultEleveShouldBeFound("matricule.in=" + DEFAULT_MATRICULE + "," + UPDATED_MATRICULE);

        // Get all the eleveList where matricule equals to UPDATED_MATRICULE
        defaultEleveShouldNotBeFound("matricule.in=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    public void getAllElevesByMatriculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where matricule is not null
        defaultEleveShouldBeFound("matricule.specified=true");

        // Get all the eleveList where matricule is null
        defaultEleveShouldNotBeFound("matricule.specified=false");
    }
                @Test
    @Transactional
    public void getAllElevesByMatriculeContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where matricule contains DEFAULT_MATRICULE
        defaultEleveShouldBeFound("matricule.contains=" + DEFAULT_MATRICULE);

        // Get all the eleveList where matricule contains UPDATED_MATRICULE
        defaultEleveShouldNotBeFound("matricule.contains=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    public void getAllElevesByMatriculeNotContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where matricule does not contain DEFAULT_MATRICULE
        defaultEleveShouldNotBeFound("matricule.doesNotContain=" + DEFAULT_MATRICULE);

        // Get all the eleveList where matricule does not contain UPDATED_MATRICULE
        defaultEleveShouldBeFound("matricule.doesNotContain=" + UPDATED_MATRICULE);
    }


    @Test
    @Transactional
    public void getAllElevesByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where prenom equals to DEFAULT_PRENOM
        defaultEleveShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the eleveList where prenom equals to UPDATED_PRENOM
        defaultEleveShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllElevesByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where prenom not equals to DEFAULT_PRENOM
        defaultEleveShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the eleveList where prenom not equals to UPDATED_PRENOM
        defaultEleveShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllElevesByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultEleveShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the eleveList where prenom equals to UPDATED_PRENOM
        defaultEleveShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllElevesByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where prenom is not null
        defaultEleveShouldBeFound("prenom.specified=true");

        // Get all the eleveList where prenom is null
        defaultEleveShouldNotBeFound("prenom.specified=false");
    }
                @Test
    @Transactional
    public void getAllElevesByPrenomContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where prenom contains DEFAULT_PRENOM
        defaultEleveShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the eleveList where prenom contains UPDATED_PRENOM
        defaultEleveShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllElevesByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where prenom does not contain DEFAULT_PRENOM
        defaultEleveShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the eleveList where prenom does not contain UPDATED_PRENOM
        defaultEleveShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }


    @Test
    @Transactional
    public void getAllElevesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where nom equals to DEFAULT_NOM
        defaultEleveShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the eleveList where nom equals to UPDATED_NOM
        defaultEleveShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllElevesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where nom not equals to DEFAULT_NOM
        defaultEleveShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the eleveList where nom not equals to UPDATED_NOM
        defaultEleveShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllElevesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultEleveShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the eleveList where nom equals to UPDATED_NOM
        defaultEleveShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllElevesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where nom is not null
        defaultEleveShouldBeFound("nom.specified=true");

        // Get all the eleveList where nom is null
        defaultEleveShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllElevesByNomContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where nom contains DEFAULT_NOM
        defaultEleveShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the eleveList where nom contains UPDATED_NOM
        defaultEleveShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllElevesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where nom does not contain DEFAULT_NOM
        defaultEleveShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the eleveList where nom does not contain UPDATED_NOM
        defaultEleveShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllElevesBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where sexe equals to DEFAULT_SEXE
        defaultEleveShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the eleveList where sexe equals to UPDATED_SEXE
        defaultEleveShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllElevesBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where sexe not equals to DEFAULT_SEXE
        defaultEleveShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the eleveList where sexe not equals to UPDATED_SEXE
        defaultEleveShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllElevesBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultEleveShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the eleveList where sexe equals to UPDATED_SEXE
        defaultEleveShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllElevesBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where sexe is not null
        defaultEleveShouldBeFound("sexe.specified=true");

        // Get all the eleveList where sexe is null
        defaultEleveShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    public void getAllElevesByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where telephone equals to DEFAULT_TELEPHONE
        defaultEleveShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the eleveList where telephone equals to UPDATED_TELEPHONE
        defaultEleveShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllElevesByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where telephone not equals to DEFAULT_TELEPHONE
        defaultEleveShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the eleveList where telephone not equals to UPDATED_TELEPHONE
        defaultEleveShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllElevesByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultEleveShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the eleveList where telephone equals to UPDATED_TELEPHONE
        defaultEleveShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllElevesByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where telephone is not null
        defaultEleveShouldBeFound("telephone.specified=true");

        // Get all the eleveList where telephone is null
        defaultEleveShouldNotBeFound("telephone.specified=false");
    }
                @Test
    @Transactional
    public void getAllElevesByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where telephone contains DEFAULT_TELEPHONE
        defaultEleveShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the eleveList where telephone contains UPDATED_TELEPHONE
        defaultEleveShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllElevesByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where telephone does not contain DEFAULT_TELEPHONE
        defaultEleveShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the eleveList where telephone does not contain UPDATED_TELEPHONE
        defaultEleveShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }


    @Test
    @Transactional
    public void getAllElevesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where email equals to DEFAULT_EMAIL
        defaultEleveShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the eleveList where email equals to UPDATED_EMAIL
        defaultEleveShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllElevesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where email not equals to DEFAULT_EMAIL
        defaultEleveShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the eleveList where email not equals to UPDATED_EMAIL
        defaultEleveShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllElevesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEleveShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the eleveList where email equals to UPDATED_EMAIL
        defaultEleveShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllElevesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where email is not null
        defaultEleveShouldBeFound("email.specified=true");

        // Get all the eleveList where email is null
        defaultEleveShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllElevesByEmailContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where email contains DEFAULT_EMAIL
        defaultEleveShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the eleveList where email contains UPDATED_EMAIL
        defaultEleveShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllElevesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where email does not contain DEFAULT_EMAIL
        defaultEleveShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the eleveList where email does not contain UPDATED_EMAIL
        defaultEleveShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllElevesByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultEleveShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the eleveList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultEleveShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByDateNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where dateNaissance not equals to DEFAULT_DATE_NAISSANCE
        defaultEleveShouldNotBeFound("dateNaissance.notEquals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the eleveList where dateNaissance not equals to UPDATED_DATE_NAISSANCE
        defaultEleveShouldBeFound("dateNaissance.notEquals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultEleveShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the eleveList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultEleveShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where dateNaissance is not null
        defaultEleveShouldBeFound("dateNaissance.specified=true");

        // Get all the eleveList where dateNaissance is null
        defaultEleveShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    public void getAllElevesByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where dateNaissance is greater than or equal to DEFAULT_DATE_NAISSANCE
        defaultEleveShouldBeFound("dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the eleveList where dateNaissance is greater than or equal to UPDATED_DATE_NAISSANCE
        defaultEleveShouldNotBeFound("dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where dateNaissance is less than or equal to DEFAULT_DATE_NAISSANCE
        defaultEleveShouldBeFound("dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the eleveList where dateNaissance is less than or equal to SMALLER_DATE_NAISSANCE
        defaultEleveShouldNotBeFound("dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where dateNaissance is less than DEFAULT_DATE_NAISSANCE
        defaultEleveShouldNotBeFound("dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the eleveList where dateNaissance is less than UPDATED_DATE_NAISSANCE
        defaultEleveShouldBeFound("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where dateNaissance is greater than DEFAULT_DATE_NAISSANCE
        defaultEleveShouldNotBeFound("dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the eleveList where dateNaissance is greater than SMALLER_DATE_NAISSANCE
        defaultEleveShouldBeFound("dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE);
    }


    @Test
    @Transactional
    public void getAllElevesByLieuNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where lieuNaissance equals to DEFAULT_LIEU_NAISSANCE
        defaultEleveShouldBeFound("lieuNaissance.equals=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the eleveList where lieuNaissance equals to UPDATED_LIEU_NAISSANCE
        defaultEleveShouldNotBeFound("lieuNaissance.equals=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByLieuNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where lieuNaissance not equals to DEFAULT_LIEU_NAISSANCE
        defaultEleveShouldNotBeFound("lieuNaissance.notEquals=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the eleveList where lieuNaissance not equals to UPDATED_LIEU_NAISSANCE
        defaultEleveShouldBeFound("lieuNaissance.notEquals=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByLieuNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where lieuNaissance in DEFAULT_LIEU_NAISSANCE or UPDATED_LIEU_NAISSANCE
        defaultEleveShouldBeFound("lieuNaissance.in=" + DEFAULT_LIEU_NAISSANCE + "," + UPDATED_LIEU_NAISSANCE);

        // Get all the eleveList where lieuNaissance equals to UPDATED_LIEU_NAISSANCE
        defaultEleveShouldNotBeFound("lieuNaissance.in=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByLieuNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where lieuNaissance is not null
        defaultEleveShouldBeFound("lieuNaissance.specified=true");

        // Get all the eleveList where lieuNaissance is null
        defaultEleveShouldNotBeFound("lieuNaissance.specified=false");
    }
                @Test
    @Transactional
    public void getAllElevesByLieuNaissanceContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where lieuNaissance contains DEFAULT_LIEU_NAISSANCE
        defaultEleveShouldBeFound("lieuNaissance.contains=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the eleveList where lieuNaissance contains UPDATED_LIEU_NAISSANCE
        defaultEleveShouldNotBeFound("lieuNaissance.contains=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllElevesByLieuNaissanceNotContainsSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList where lieuNaissance does not contain DEFAULT_LIEU_NAISSANCE
        defaultEleveShouldNotBeFound("lieuNaissance.doesNotContain=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the eleveList where lieuNaissance does not contain UPDATED_LIEU_NAISSANCE
        defaultEleveShouldBeFound("lieuNaissance.doesNotContain=" + UPDATED_LIEU_NAISSANCE);
    }


    @Test
    @Transactional
    public void getAllElevesByTuteurIsEqualToSomething() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);
        Tuteur tuteur = TuteurResourceIT.createEntity(em);
        em.persist(tuteur);
        em.flush();
        eleve.setTuteur(tuteur);
        eleveRepository.saveAndFlush(eleve);
        String tuteurId = tuteur.getId();

        // Get all the eleveList where tuteur equals to tuteurId
        defaultEleveShouldBeFound("tuteurId.equals=" + tuteurId);

        // Get all the eleveList where tuteur equals to tuteurId + 1
        defaultEleveShouldNotBeFound("tuteurId.equals=" + (tuteurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEleveShouldBeFound(String filter) throws Exception {
        restEleveMockMvc.perform(get("/api/eleves?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eleve.getId())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)));

        // Check, that the count call also returns 1
        restEleveMockMvc.perform(get("/api/eleves/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEleveShouldNotBeFound(String filter) throws Exception {
        restEleveMockMvc.perform(get("/api/eleves?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEleveMockMvc.perform(get("/api/eleves/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEleve() throws Exception {
        // Get the eleve
        restEleveMockMvc.perform(get("/api/eleves/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();

        // Update the eleve
        Eleve updatedEleve = eleveRepository.findById(eleve.getId()).get();
        // Disconnect from session so that the updates on updatedEleve are not directly saved in db
        em.detach(updatedEleve);

            updatedEleve.setPhoto(UPDATED_PHOTO);
            updatedEleve.setPhotoContentType(UPDATED_PHOTO_CONTENT_TYPE);
            updatedEleve.setMatricule(UPDATED_MATRICULE);
            updatedEleve.setPrenom(UPDATED_PRENOM);
            updatedEleve.setNom(UPDATED_NOM);
            updatedEleve.setSexe(UPDATED_SEXE);
            updatedEleve.setAdresse(UPDATED_ADRESSE);
            updatedEleve.setTelephone(UPDATED_TELEPHONE);
            updatedEleve.setEmail(UPDATED_EMAIL);
            updatedEleve.setDateNaissance(UPDATED_DATE_NAISSANCE);
            updatedEleve.setLieuNaissance(UPDATED_LIEU_NAISSANCE);
        EleveDTO eleveDTO = eleveMapper.toDto(updatedEleve);

        restEleveMockMvc.perform(put("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isOk());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
        Eleve testEleve = eleveList.get(eleveList.size() - 1);
        assertThat(testEleve.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEleve.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testEleve.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testEleve.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEleve.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEleve.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEleve.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEleve.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEleve.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEleve.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testEleve.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingEleve() throws Exception {
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();

        // Create the Eleve
        EleveDTO eleveDTO = eleveMapper.toDto(eleve);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEleveMockMvc.perform(put("/api/eleves").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eleveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        int databaseSizeBeforeDelete = eleveRepository.findAll().size();

        // Delete the eleve
        restEleveMockMvc.perform(delete("/api/eleves/{id}", eleve.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
