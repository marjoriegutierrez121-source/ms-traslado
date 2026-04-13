package pe.inpe.ms_traslado.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;
import pe.inpe.ms_traslado.exception.BusinessException;
import pe.inpe.ms_traslado.exception.ResourceNotFoundException;
import pe.inpe.ms_traslado.mapper.TrasladoCustodiaMapper;
import pe.inpe.ms_traslado.repository.TrasladoCustodiaRepository;
import pe.inpe.ms_traslado.repository.TrasladoRepository;
import pe.inpe.ms_traslado.service.TrasladoCustodiaService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrasladoCustodiaServiceImpl implements TrasladoCustodiaService {

    private final TrasladoCustodiaRepository custodiaRepository;
    private final TrasladoRepository trasladoRepository;
    private final TrasladoCustodiaMapper custodiaMapper;

    @Override
    public TrasladoCustodiaResponseDTO asignarCustodia(Long idTraslado, TrasladoCustodiaRequestDTO dto) {
        log.info("Asignando custodia - traslado id: {}, personal id: {}", idTraslado, dto.getIdPersonal());

        Traslado traslado = buscarTrasladoPorId(idTraslado);
        validarCustodioNoAsignado(idTraslado, dto.getIdPersonal());

        TrasladoCustodia custodia = custodiaMapper.toEntity(dto);
        custodia.setTraslado(traslado);

        return custodiaMapper.toDto(custodiaRepository.save(custodia));
    }

    @Override
    public void removerCustodia(Long idTraslado, Long idPersonal) {
        log.info("Removiendo custodia - traslado id: {}, personal id: {}", idTraslado, idPersonal);

        if (!custodiaRepository.existsByTrasladoIdTrasladoAndIdPersonal(idTraslado, idPersonal)) {
            throw new ResourceNotFoundException(
                    "Custodio con personal id " + idPersonal + " no asignado al traslado id " + idTraslado);
        }

        custodiaRepository.deleteByTrasladoIdTrasladoAndIdPersonal(idTraslado, idPersonal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoCustodiaResponseDTO> listarPorTraslado(Long idTraslado) {
        log.info("Listando custodios del traslado id: {}", idTraslado);
        buscarTrasladoPorId(idTraslado); // valida que el traslado exista
        return custodiaMapper.toDtoList(custodiaRepository.findByTrasladoIdTraslado(idTraslado));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoCustodiaResponseDTO> listarPorPersonal(Long idPersonal) {
        log.info("Listando traslados del personal id: {}", idPersonal);
        return custodiaMapper.toDtoList(custodiaRepository.findByIdPersonal(idPersonal));
    }

    private Traslado buscarTrasladoPorId(Long idTraslado) {
        return trasladoRepository.findById(idTraslado)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Traslado no encontrado con id: " + idTraslado));
    }

    private void validarCustodioNoAsignado(Long idTraslado, Long idPersonal) {
        if (custodiaRepository.existsByTrasladoIdTrasladoAndIdPersonal(idTraslado, idPersonal)) {
            throw new BusinessException(
                    "El personal id " + idPersonal + " ya está asignado al traslado id " + idTraslado);
        }
    }
}