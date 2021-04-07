package br.com.consiga.service.impl;

import br.com.consiga.domain.Estado;
import br.com.consiga.repository.EstadoRepository;
import br.com.consiga.service.EstadoService;
import br.com.consiga.service.dto.EstadoDTO;
import br.com.consiga.service.mapper.EstadoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Estado}.
 */
@Service
@Transactional
public class EstadoServiceImpl implements EstadoService {

    private final Logger log = LoggerFactory.getLogger(EstadoServiceImpl.class);

    private final EstadoRepository estadoRepository;

    private final EstadoMapper estadoMapper;

    public EstadoServiceImpl(EstadoRepository estadoRepository, EstadoMapper estadoMapper) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
    }

    @Override
    public EstadoDTO save(EstadoDTO estadoDTO) {
        log.debug("Request to save Estado : {}", estadoDTO);
        Estado estado = estadoMapper.toEntity(estadoDTO);
        estado = estadoRepository.save(estado);
        return estadoMapper.toDto(estado);
    }

    @Override
    public Optional<EstadoDTO> partialUpdate(EstadoDTO estadoDTO) {
        log.debug("Request to partially update Estado : {}", estadoDTO);

        return estadoRepository
            .findById(estadoDTO.getId())
            .map(
                existingEstado -> {
                    estadoMapper.partialUpdate(existingEstado, estadoDTO);
                    return existingEstado;
                }
            )
            .map(estadoRepository::save)
            .map(estadoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstadoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estados");
        return estadoRepository.findAll(pageable).map(estadoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoDTO> findOne(Long id) {
        log.debug("Request to get Estado : {}", id);
        return estadoRepository.findById(id).map(estadoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Estado : {}", id);
        estadoRepository.deleteById(id);
    }
}
