package br.com.consiga.service.impl;

import br.com.consiga.domain.Funcionario;
import br.com.consiga.repository.FuncionarioRepository;
import br.com.consiga.service.FuncionarioService;
import br.com.consiga.service.dto.FuncionarioDTO;
import br.com.consiga.service.mapper.FuncionarioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Funcionario}.
 */
@Service
@Transactional
public class FuncionarioServiceImpl implements FuncionarioService {

    private final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);

    private final FuncionarioRepository funcionarioRepository;

    private final FuncionarioMapper funcionarioMapper;

    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository, FuncionarioMapper funcionarioMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioMapper = funcionarioMapper;
    }

    @Override
    public FuncionarioDTO save(FuncionarioDTO funcionarioDTO) {
        log.debug("Request to save Funcionario : {}", funcionarioDTO);
        Funcionario funcionario = funcionarioMapper.toEntity(funcionarioDTO);
        funcionario = funcionarioRepository.save(funcionario);
        return funcionarioMapper.toDto(funcionario);
    }

    @Override
    public Optional<FuncionarioDTO> partialUpdate(FuncionarioDTO funcionarioDTO) {
        log.debug("Request to partially update Funcionario : {}", funcionarioDTO);

        return funcionarioRepository
            .findById(funcionarioDTO.getId())
            .map(
                existingFuncionario -> {
                    funcionarioMapper.partialUpdate(existingFuncionario, funcionarioDTO);
                    return existingFuncionario;
                }
            )
            .map(funcionarioRepository::save)
            .map(funcionarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FuncionarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Funcionarios");
        return funcionarioRepository.findAll(pageable).map(funcionarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FuncionarioDTO> findOne(Long id) {
        log.debug("Request to get Funcionario : {}", id);
        return funcionarioRepository.findById(id).map(funcionarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Funcionario : {}", id);
        funcionarioRepository.deleteById(id);
    }
}
