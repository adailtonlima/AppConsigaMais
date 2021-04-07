package br.com.consiga.service.impl;

import br.com.consiga.domain.Administrador;
import br.com.consiga.repository.AdministradorRepository;
import br.com.consiga.service.AdministradorService;
import br.com.consiga.service.dto.AdministradorDTO;
import br.com.consiga.service.mapper.AdministradorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Administrador}.
 */
@Service
@Transactional
public class AdministradorServiceImpl implements AdministradorService {

    private final Logger log = LoggerFactory.getLogger(AdministradorServiceImpl.class);

    private final AdministradorRepository administradorRepository;

    private final AdministradorMapper administradorMapper;

    public AdministradorServiceImpl(AdministradorRepository administradorRepository, AdministradorMapper administradorMapper) {
        this.administradorRepository = administradorRepository;
        this.administradorMapper = administradorMapper;
    }

    @Override
    public AdministradorDTO save(AdministradorDTO administradorDTO) {
        log.debug("Request to save Administrador : {}", administradorDTO);
        Administrador administrador = administradorMapper.toEntity(administradorDTO);
        administrador = administradorRepository.save(administrador);
        return administradorMapper.toDto(administrador);
    }

    @Override
    public Optional<AdministradorDTO> partialUpdate(AdministradorDTO administradorDTO) {
        log.debug("Request to partially update Administrador : {}", administradorDTO);

        return administradorRepository
            .findById(administradorDTO.getId())
            .map(
                existingAdministrador -> {
                    administradorMapper.partialUpdate(existingAdministrador, administradorDTO);
                    return existingAdministrador;
                }
            )
            .map(administradorRepository::save)
            .map(administradorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdministradorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Administradors");
        return administradorRepository.findAll(pageable).map(administradorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdministradorDTO> findOne(Long id) {
        log.debug("Request to get Administrador : {}", id);
        return administradorRepository.findById(id).map(administradorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Administrador : {}", id);
        administradorRepository.deleteById(id);
    }
}
