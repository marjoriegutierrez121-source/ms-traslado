package pe.inpe.ms_traslado.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;
import pe.inpe.ms_traslado.mapper.TrasladoCustodiaMapper;
import pe.inpe.ms_traslado.mapper.TrasladoMapper;
import pe.inpe.ms_traslado.repository.TrasladoCustodiaRepository;
import pe.inpe.ms_traslado.repository.TrasladoRepository;
import pe.inpe.ms_traslado.service.TrasladoCustodiaService;
import pe.inpe.ms_traslado.util.TrasladoEstados;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrasladoCustodiaServiceImpl implements TrasladoCustodiaService {

    private final TrasladoRepository trasladoRepository;
    private final TrasladoCustodiaRepository trasladoCustodiaRepository;
    private final TrasladoCustodiaMapper trasladoCustodiaMapper;
    private final TrasladoMapper trasladoMapper;

    @Override
    @Transactional
    public TrasladoCustodiaResponseDTO addCustodia(Long idTraslado, TrasladoCustodiaRequestDTO requestDto) {
        log.info("Asignando custodio {} al traslado {}", requestDto.getIdPersonal(), idTraslado);

        Traslado traslado = trasladoRepository.findById(idTraslado)
                .orElseThrow(() -> new RuntimeException("Traslado no encontrado con ID: " + idTraslado));

        // Solo se puede asignar custodios si está PROGRAMADO
        if (!TrasladoEstados.PROGRAMADO.equals(traslado.getEstadoTrasladoId())) {
            throw new IllegalStateException("Solo se pueden asignar custodios en traslados PROGRAMADOS");
        }

        // Validar que el mismo personal no esté ya asignado
        if (trasladoCustodiaRepository.existsByTrasladoAndIdPersonal(traslado, requestDto.getIdPersonal())) {
            throw new IllegalStateException("El personal ya está asignado como custodio de este traslado");
        }

        TrasladoCustodia custodia = trasladoCustodiaMapper.toEntity(requestDto);
        custodia.setTraslado(traslado);

        // Datos de auditoría
        custodia.setRegistrationDate(LocalDateTime.now());
        custodia.setRegistrationUser("SYSTEM");
        custodia.setLastModificationDate(LocalDateTime.now());
        custodia.setLastModificationUser("SYSTEM");

        TrasladoCustodia saved = trasladoCustodiaRepository.save(custodia);
        log.info("Custodio asignado exitosamente con ID: {}", saved.getIdTrasladoCustodia());

        return trasladoCustodiaMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void removeCustodia(Long idTraslado, Long idPersonal) {
        log.info("Removiendo custodio {} del traslado {}", idPersonal, idTraslado);

        Traslado traslado = trasladoRepository.findById(idTraslado)
                .orElseThrow(() -> new RuntimeException("Traslado no encontrado con ID: " + idTraslado));

        // Solo se puede remover custodios si está PROGRAMADO
        if (!TrasladoEstados.PROGRAMADO.equals(traslado.getEstadoTrasladoId())) {
            throw new IllegalStateException("Solo se pueden remover custodios en traslados PROGRAMADOS");
        }

        TrasladoCustodia custodia = trasladoCustodiaRepository
                .findByTrasladoAndIdPersonal(traslado, idPersonal)
                .orElseThrow(() -> new RuntimeException("Custodio no encontrado para este traslado"));

        trasladoCustodiaRepository.delete(custodia);
        log.info("Custodio removido exitosamente");
    }

    @Override
    public List<TrasladoCustodiaResponseDTO> getCustodiasXTraslado(Long idTraslado) {
        log.info("Listando custodios del traslado: {}", idTraslado);

        Traslado traslado = trasladoRepository.findById(idTraslado)
                .orElseThrow(() -> new RuntimeException("Traslado no encontrado con ID: " + idTraslado));

        List<TrasladoCustodia> custodias = trasladoCustodiaRepository.findByTraslado(traslado);
        return trasladoCustodiaMapper.toResponseDTOList(custodias);
    }

    @Override
    public List<TrasladoResponseDTO> getTrasladosXCustodio(Long idPersonal) {
        log.info("Listando traslados del custodio: {}", idPersonal);

        List<Traslado> traslados = trasladoCustodiaRepository.findTrasladosByPersonal(idPersonal);
        return trasladoMapper.toResponseDTOList(traslados);
    }
}
