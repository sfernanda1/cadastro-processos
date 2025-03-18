package com.desafio.tecnico.web.rest;

import static com.desafio.tecnico.domain.ProcessoAsserts.*;
import static com.desafio.tecnico.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.desafio.tecnico.IntegrationTest;
import com.desafio.tecnico.domain.Processo;
import com.desafio.tecnico.repository.ProcessoRepository;
import com.desafio.tecnico.service.dto.ProcessoDTO;
import com.desafio.tecnico.service.mapper.ProcessoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProcessoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessoResourceIT {

    private static final String DEFAULT_NPU = "AAAAAAAAAA";
    private static final String UPDATED_NPU = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CADASTRO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_VISUALIZACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VISUALIZACAO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_MUNICIPIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_MUNICIPIO = 1;
    private static final Integer UPDATED_ID_MUNICIPIO = 2;

    private static final String DEFAULT_UF = "AAAAAAAAAA";
    private static final String UPDATED_UF = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_UF = 1;
    private static final Integer UPDATED_ID_UF = 2;

    private static final String DEFAULT_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/processos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ProcessoMapper processoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessoMockMvc;

    private Processo processo;

    private Processo insertedProcesso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processo createEntity() {
        return new Processo()
            .npu(DEFAULT_NPU)
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .dataVisualizacao(DEFAULT_DATA_VISUALIZACAO)
            .municipio(DEFAULT_MUNICIPIO)
            .uf(DEFAULT_UF)
            .documento(DEFAULT_DOCUMENTO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processo createUpdatedEntity() {
        return new Processo()
            .npu(UPDATED_NPU)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataVisualizacao(UPDATED_DATA_VISUALIZACAO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .documento(UPDATED_DOCUMENTO);
    }

    @BeforeEach
    public void initTest() {
        processo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProcesso != null) {
            processoRepository.delete(insertedProcesso);
            insertedProcesso = null;
        }
    }

    @Test
    @Transactional
    void createProcesso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);
        var returnedProcessoDTO = om.readValue(
            restProcessoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProcessoDTO.class
        );

        // Validate the Processo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProcesso = processoMapper.toEntity(returnedProcessoDTO);
        assertProcessoUpdatableFieldsEquals(returnedProcesso, getPersistedProcesso(returnedProcesso));

        insertedProcesso = returnedProcesso;
    }

    @Test
    @Transactional
    void createProcessoWithExistingId() throws Exception {
        // Create the Processo with an existing ID
        processo.setId(1L);
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcessos() throws Exception {
        // Initialize the database
        insertedProcesso = processoRepository.saveAndFlush(processo);

        // Get all the processoList
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processo.getId().intValue())))
            .andExpect(jsonPath("$.[*].npu").value(hasItem(DEFAULT_NPU)))
            .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].dataVisualizacao").value(hasItem(DEFAULT_DATA_VISUALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO)));
    }

    @Test
    @Transactional
    void getProcesso() throws Exception {
        // Initialize the database
        insertedProcesso = processoRepository.saveAndFlush(processo);

        // Get the processo
        restProcessoMockMvc
            .perform(get(ENTITY_API_URL_ID, processo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processo.getId().intValue()))
            .andExpect(jsonPath("$.npu").value(DEFAULT_NPU))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO.toString()))
            .andExpect(jsonPath("$.dataVisualizacao").value(DEFAULT_DATA_VISUALIZACAO.toString()))
            .andExpect(jsonPath("$.municipio").value(DEFAULT_MUNICIPIO))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO));
    }

    @Test
    @Transactional
    void getNonExistingProcesso() throws Exception {
        // Get the processo
        restProcessoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcesso() throws Exception {
        // Initialize the database
        insertedProcesso = processoRepository.saveAndFlush(processo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processo
        Processo updatedProcesso = processoRepository.findById(processo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProcesso are not directly saved in db
        em.detach(updatedProcesso);
        updatedProcesso
            .npu(UPDATED_NPU)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataVisualizacao(UPDATED_DATA_VISUALIZACAO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .documento(UPDATED_DOCUMENTO);
        ProcessoDTO processoDTO = processoMapper.toDto(updatedProcesso);

        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProcessoToMatchAllProperties(updatedProcesso);
    }

    @Test
    @Transactional
    void putNonExistingProcesso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processo.setId(longCount.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcesso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processo.setId(longCount.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcesso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processo.setId(longCount.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(processoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessoWithPatch() throws Exception {
        // Initialize the database
        insertedProcesso = processoRepository.saveAndFlush(processo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processo using partial update
        Processo partialUpdatedProcesso = new Processo();
        partialUpdatedProcesso.setId(processo.getId());

        partialUpdatedProcesso
            .npu(UPDATED_NPU)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .municipio(UPDATED_MUNICIPIO)
            .documento(UPDATED_DOCUMENTO);

        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcesso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProcesso))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcessoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProcesso, processo), getPersistedProcesso(processo));
    }

    @Test
    @Transactional
    void fullUpdateProcessoWithPatch() throws Exception {
        // Initialize the database
        insertedProcesso = processoRepository.saveAndFlush(processo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processo using partial update
        Processo partialUpdatedProcesso = new Processo();
        partialUpdatedProcesso.setId(processo.getId());

        partialUpdatedProcesso
            .npu(UPDATED_NPU)
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .dataVisualizacao(UPDATED_DATA_VISUALIZACAO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .documento(UPDATED_DOCUMENTO);

        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcesso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProcesso))
            )
            .andExpect(status().isOk());

        // Validate the Processo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcessoUpdatableFieldsEquals(partialUpdatedProcesso, getPersistedProcesso(partialUpdatedProcesso));
    }

    @Test
    @Transactional
    void patchNonExistingProcesso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processo.setId(longCount.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcesso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processo.setId(longCount.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(processoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Processo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcesso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processo.setId(longCount.incrementAndGet());

        // Create the Processo
        ProcessoDTO processoDTO = processoMapper.toDto(processo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(processoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Processo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcesso() throws Exception {
        // Initialize the database
        insertedProcesso = processoRepository.saveAndFlush(processo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the processo
        restProcessoMockMvc
            .perform(delete(ENTITY_API_URL_ID, processo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return processoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Processo getPersistedProcesso(Processo processo) {
        return processoRepository.findById(processo.getId()).orElseThrow();
    }

    protected void assertPersistedProcessoToMatchAllProperties(Processo expectedProcesso) {
        assertProcessoAllPropertiesEquals(expectedProcesso, getPersistedProcesso(expectedProcesso));
    }

    protected void assertPersistedProcessoToMatchUpdatableProperties(Processo expectedProcesso) {
        assertProcessoAllUpdatablePropertiesEquals(expectedProcesso, getPersistedProcesso(expectedProcesso));
    }
}
