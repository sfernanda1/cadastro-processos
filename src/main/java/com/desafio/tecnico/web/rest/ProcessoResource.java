package com.desafio.tecnico.web.rest;

import com.desafio.tecnico.repository.ProcessoRepository;
import com.desafio.tecnico.service.ProcessoService;
import com.desafio.tecnico.service.dto.ProcessoDTO;
import com.desafio.tecnico.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.desafio.tecnico.domain.Processo}.
 */
@RestController
@RequestMapping("/api/processos")
public class ProcessoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessoResource.class);

    private static final String ENTITY_NAME = "processo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessoService processoService;

    private final ProcessoRepository processoRepository;

    public ProcessoResource(ProcessoService processoService, ProcessoRepository processoRepository) {
        this.processoService = processoService;
        this.processoRepository = processoRepository;
    }

    /**
     * {@code POST  /processos} : Create a new processo.
     *
     * @param processoDTO the processoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processoDTO, or with status {@code 400 (Bad Request)} if the processo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProcessoDTO> createProcesso(@RequestBody ProcessoDTO processoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Processo : {}", processoDTO);
        if (processoDTO.getId() != null) {
            throw new BadRequestAlertException("A new processo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        processoDTO = processoService.save(processoDTO);
        return ResponseEntity.created(new URI("/api/processos/" + processoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, processoDTO.getId().toString()))
            .body(processoDTO);
    }

    /**
     * {@code PUT  /processos/:id} : Updates an existing processo.
     *
     * @param id the id of the processoDTO to save.
     * @param processoDTO the processoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processoDTO,
     * or with status {@code 400 (Bad Request)} if the processoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcessoDTO> updateProcesso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessoDTO processoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Processo : {}, {}", id, processoDTO);
        if (processoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        processoDTO = processoService.update(processoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processoDTO.getId().toString()))
            .body(processoDTO);
    }

    /**
     * {@code PATCH  /processos/:id} : Partial updates given fields of an existing processo, field will ignore if it is null
     *
     * @param id the id of the processoDTO to save.
     * @param processoDTO the processoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processoDTO,
     * or with status {@code 400 (Bad Request)} if the processoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the processoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the processoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessoDTO> partialUpdateProcesso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessoDTO processoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Processo partially : {}, {}", id, processoDTO);
        if (processoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessoDTO> result = processoService.partialUpdate(processoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, processoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /processos} : get all the processos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProcessoDTO>> getAllProcessos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Processos");
        Page<ProcessoDTO> page = processoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /processos/:id} : get the "id" processo.
     *
     * @param id the id of the processoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcessoDTO> getProcesso(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Processo : {}", id);
        Optional<ProcessoDTO> processoDTO = processoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processoDTO);
    }

    /**
     * {@code DELETE  /processos/:id} : delete the "id" processo.
     *
     * @param id the id of the processoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcesso(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Processo : {}", id);
        processoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
