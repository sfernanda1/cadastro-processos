package com.desafio.tecnico.service.mapper;

import com.desafio.tecnico.domain.Processo;
import com.desafio.tecnico.service.dto.ProcessoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Processo} and its DTO {@link ProcessoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProcessoMapper extends EntityMapper<ProcessoDTO, Processo> {}
