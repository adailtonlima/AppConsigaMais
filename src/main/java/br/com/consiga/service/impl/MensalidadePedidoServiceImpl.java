package br.com.consiga.service.impl;

import br.com.consiga.domain.MensalidadePedido;
import br.com.consiga.repository.MensalidadePedidoRepository;
import br.com.consiga.service.MensalidadePedidoService;
import br.com.consiga.service.dto.MensalidadePedidoDTO;
import br.com.consiga.service.mapper.MensalidadePedidoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MensalidadePedido}.
 */
@Service
@Transactional
public class MensalidadePedidoServiceImpl implements MensalidadePedidoService {

    private final Logger log = LoggerFactory.getLogger(MensalidadePedidoServiceImpl.class);

    private final MensalidadePedidoRepository mensalidadePedidoRepository;

    private final MensalidadePedidoMapper mensalidadePedidoMapper;

    public MensalidadePedidoServiceImpl(
        MensalidadePedidoRepository mensalidadePedidoRepository,
        MensalidadePedidoMapper mensalidadePedidoMapper
    ) {
        this.mensalidadePedidoRepository = mensalidadePedidoRepository;
        this.mensalidadePedidoMapper = mensalidadePedidoMapper;
    }

    @Override
    public MensalidadePedidoDTO save(MensalidadePedidoDTO mensalidadePedidoDTO) {
        log.debug("Request to save MensalidadePedido : {}", mensalidadePedidoDTO);
        MensalidadePedido mensalidadePedido = mensalidadePedidoMapper.toEntity(mensalidadePedidoDTO);
        mensalidadePedido = mensalidadePedidoRepository.save(mensalidadePedido);
        return mensalidadePedidoMapper.toDto(mensalidadePedido);
    }

    @Override
    public Optional<MensalidadePedidoDTO> partialUpdate(MensalidadePedidoDTO mensalidadePedidoDTO) {
        log.debug("Request to partially update MensalidadePedido : {}", mensalidadePedidoDTO);

        return mensalidadePedidoRepository
            .findById(mensalidadePedidoDTO.getId())
            .map(
                existingMensalidadePedido -> {
                    mensalidadePedidoMapper.partialUpdate(existingMensalidadePedido, mensalidadePedidoDTO);
                    return existingMensalidadePedido;
                }
            )
            .map(mensalidadePedidoRepository::save)
            .map(mensalidadePedidoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MensalidadePedidoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MensalidadePedidos");
        return mensalidadePedidoRepository.findAll(pageable).map(mensalidadePedidoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MensalidadePedidoDTO> findOne(Long id) {
        log.debug("Request to get MensalidadePedido : {}", id);
        return mensalidadePedidoRepository.findById(id).map(mensalidadePedidoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MensalidadePedido : {}", id);
        mensalidadePedidoRepository.deleteById(id);
    }
}
