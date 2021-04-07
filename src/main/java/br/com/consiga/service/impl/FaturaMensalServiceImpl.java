package br.com.consiga.service.impl;

import br.com.consiga.domain.FaturaMensal;
import br.com.consiga.repository.FaturaMensalRepository;
import br.com.consiga.service.FaturaMensalService;
import br.com.consiga.service.dto.FaturaMensalDTO;
import br.com.consiga.service.mapper.FaturaMensalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FaturaMensal}.
 */
@Service
@Transactional
public class FaturaMensalServiceImpl implements FaturaMensalService {

    private final Logger log = LoggerFactory.getLogger(FaturaMensalServiceImpl.class);

    private final FaturaMensalRepository faturaMensalRepository;

    private final FaturaMensalMapper faturaMensalMapper;

    public FaturaMensalServiceImpl(FaturaMensalRepository faturaMensalRepository, FaturaMensalMapper faturaMensalMapper) {
        this.faturaMensalRepository = faturaMensalRepository;
        this.faturaMensalMapper = faturaMensalMapper;
    }

    @Override
    public FaturaMensalDTO save(FaturaMensalDTO faturaMensalDTO) {
        log.debug("Request to save FaturaMensal : {}", faturaMensalDTO);
        FaturaMensal faturaMensal = faturaMensalMapper.toEntity(faturaMensalDTO);
        faturaMensal = faturaMensalRepository.save(faturaMensal);
        return faturaMensalMapper.toDto(faturaMensal);
    }

    @Override
    public Optional<FaturaMensalDTO> partialUpdate(FaturaMensalDTO faturaMensalDTO) {
        log.debug("Request to partially update FaturaMensal : {}", faturaMensalDTO);

        return faturaMensalRepository
            .findById(faturaMensalDTO.getId())
            .map(
                existingFaturaMensal -> {
                    faturaMensalMapper.partialUpdate(existingFaturaMensal, faturaMensalDTO);
                    return existingFaturaMensal;
                }
            )
            .map(faturaMensalRepository::save)
            .map(faturaMensalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FaturaMensalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FaturaMensals");
        return faturaMensalRepository.findAll(pageable).map(faturaMensalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FaturaMensalDTO> findOne(Long id) {
        log.debug("Request to get FaturaMensal : {}", id);
        return faturaMensalRepository.findById(id).map(faturaMensalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FaturaMensal : {}", id);
        faturaMensalRepository.deleteById(id);
    }
}
