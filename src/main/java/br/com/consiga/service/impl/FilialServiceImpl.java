package br.com.consiga.service.impl;

import br.com.consiga.domain.Filial;
import br.com.consiga.repository.FilialRepository;
import br.com.consiga.service.FilialService;
import br.com.consiga.service.dto.FilialDTO;
import br.com.consiga.service.mapper.FilialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Filial}.
 */
@Service
@Transactional
public class FilialServiceImpl implements FilialService {

    private final Logger log = LoggerFactory.getLogger(FilialServiceImpl.class);

    private final FilialRepository filialRepository;

    private final FilialMapper filialMapper;

    public FilialServiceImpl(FilialRepository filialRepository, FilialMapper filialMapper) {
        this.filialRepository = filialRepository;
        this.filialMapper = filialMapper;
    }

    @Override
    public FilialDTO save(FilialDTO filialDTO) {
        log.debug("Request to save Filial : {}", filialDTO);
        Filial filial = filialMapper.toEntity(filialDTO);
        filial = filialRepository.save(filial);
        return filialMapper.toDto(filial);
    }

    @Override
    public Optional<FilialDTO> partialUpdate(FilialDTO filialDTO) {
        log.debug("Request to partially update Filial : {}", filialDTO);

        return filialRepository
            .findById(filialDTO.getId())
            .map(
                existingFilial -> {
                    filialMapper.partialUpdate(existingFilial, filialDTO);
                    return existingFilial;
                }
            )
            .map(filialRepository::save)
            .map(filialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Filials");
        return filialRepository.findAll(pageable).map(filialMapper::toDto);
    }

    public Page<FilialDTO> findAllWithEagerRelationships(Pageable pageable) {
        return filialRepository.findAllWithEagerRelationships(pageable).map(filialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FilialDTO> findOne(Long id) {
        log.debug("Request to get Filial : {}", id);
        return filialRepository.findOneWithEagerRelationships(id).map(filialMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Filial : {}", id);
        filialRepository.deleteById(id);
    }
}
