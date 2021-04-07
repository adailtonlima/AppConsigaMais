package br.com.consiga.service.impl;

import br.com.consiga.domain.Cidade;
import br.com.consiga.repository.CidadeRepository;
import br.com.consiga.service.CidadeService;
import br.com.consiga.service.dto.CidadeDTO;
import br.com.consiga.service.mapper.CidadeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cidade}.
 */
@Service
@Transactional
public class CidadeServiceImpl implements CidadeService {

    private final Logger log = LoggerFactory.getLogger(CidadeServiceImpl.class);

    private final CidadeRepository cidadeRepository;

    private final CidadeMapper cidadeMapper;

    public CidadeServiceImpl(CidadeRepository cidadeRepository, CidadeMapper cidadeMapper) {
        this.cidadeRepository = cidadeRepository;
        this.cidadeMapper = cidadeMapper;
    }

    @Override
    public CidadeDTO save(CidadeDTO cidadeDTO) {
        log.debug("Request to save Cidade : {}", cidadeDTO);
        Cidade cidade = cidadeMapper.toEntity(cidadeDTO);
        cidade = cidadeRepository.save(cidade);
        return cidadeMapper.toDto(cidade);
    }

    @Override
    public Optional<CidadeDTO> partialUpdate(CidadeDTO cidadeDTO) {
        log.debug("Request to partially update Cidade : {}", cidadeDTO);

        return cidadeRepository
            .findById(cidadeDTO.getId())
            .map(
                existingCidade -> {
                    cidadeMapper.partialUpdate(existingCidade, cidadeDTO);
                    return existingCidade;
                }
            )
            .map(cidadeRepository::save)
            .map(cidadeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CidadeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cidades");
        return cidadeRepository.findAll(pageable).map(cidadeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CidadeDTO> findOne(Long id) {
        log.debug("Request to get Cidade : {}", id);
        return cidadeRepository.findById(id).map(cidadeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cidade : {}", id);
        cidadeRepository.deleteById(id);
    }
}
