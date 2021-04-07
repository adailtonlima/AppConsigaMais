package br.com.consiga.service.impl;

import br.com.consiga.domain.ArquivoImportacao;
import br.com.consiga.repository.ArquivoImportacaoRepository;
import br.com.consiga.service.ArquivoImportacaoService;
import br.com.consiga.service.dto.ArquivoImportacaoDTO;
import br.com.consiga.service.mapper.ArquivoImportacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ArquivoImportacao}.
 */
@Service
@Transactional
public class ArquivoImportacaoServiceImpl implements ArquivoImportacaoService {

    private final Logger log = LoggerFactory.getLogger(ArquivoImportacaoServiceImpl.class);

    private final ArquivoImportacaoRepository arquivoImportacaoRepository;

    private final ArquivoImportacaoMapper arquivoImportacaoMapper;

    public ArquivoImportacaoServiceImpl(
        ArquivoImportacaoRepository arquivoImportacaoRepository,
        ArquivoImportacaoMapper arquivoImportacaoMapper
    ) {
        this.arquivoImportacaoRepository = arquivoImportacaoRepository;
        this.arquivoImportacaoMapper = arquivoImportacaoMapper;
    }

    @Override
    public ArquivoImportacaoDTO save(ArquivoImportacaoDTO arquivoImportacaoDTO) {
        log.debug("Request to save ArquivoImportacao : {}", arquivoImportacaoDTO);
        ArquivoImportacao arquivoImportacao = arquivoImportacaoMapper.toEntity(arquivoImportacaoDTO);
        arquivoImportacao = arquivoImportacaoRepository.save(arquivoImportacao);
        return arquivoImportacaoMapper.toDto(arquivoImportacao);
    }

    @Override
    public Optional<ArquivoImportacaoDTO> partialUpdate(ArquivoImportacaoDTO arquivoImportacaoDTO) {
        log.debug("Request to partially update ArquivoImportacao : {}", arquivoImportacaoDTO);

        return arquivoImportacaoRepository
            .findById(arquivoImportacaoDTO.getId())
            .map(
                existingArquivoImportacao -> {
                    arquivoImportacaoMapper.partialUpdate(existingArquivoImportacao, arquivoImportacaoDTO);
                    return existingArquivoImportacao;
                }
            )
            .map(arquivoImportacaoRepository::save)
            .map(arquivoImportacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArquivoImportacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArquivoImportacaos");
        return arquivoImportacaoRepository.findAll(pageable).map(arquivoImportacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArquivoImportacaoDTO> findOne(Long id) {
        log.debug("Request to get ArquivoImportacao : {}", id);
        return arquivoImportacaoRepository.findById(id).map(arquivoImportacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArquivoImportacao : {}", id);
        arquivoImportacaoRepository.deleteById(id);
    }
}
