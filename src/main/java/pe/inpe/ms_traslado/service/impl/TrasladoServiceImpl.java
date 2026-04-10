package pe.inpe.ms_traslado.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;
import pe.inpe.ms_traslado.exception.BadRequestException;
import pe.inpe.ms_traslado.exception.ResourceNotFoundException;
import pe.inpe.ms_traslado.mapper.TrasladoCustodiaMapper;
import pe.inpe.ms_traslado.mapper.TrasladoMapper;
import pe.inpe.ms_traslado.repository.TrasladoCustodiaRepository;
import pe.inpe.ms_traslado.repository.TrasladoRepository;
import pe.inpe.ms_traslado.service.TrasladoService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrasladoServiceImpl implements TrasladoService {

    public final TrasladoRepository trasladoRepository;
    public final TrasladoCustodiaRepository trasladoCustodiaRepository;
    public final TrasladoMapper trasladoMapper;
    public final TrasladoCustodiaMapper trasladoCustodiaMapper;

    @Override
    @Transactional
    public TrasladoResponseDTO addTraslado(TrasladoRequestDTO trasladoDto) {
        trasladoRepository.findByIdInternoAndEstado(trasladoDto.getIdInterno(), true)
                .ifPresent(t -> {
                    throw new BadRequestException(
                            "El interno con id " + trasladoDto.getIdInterno() + " ya tiene un traslado activo"
                    );
                });

        if (trasladoDto.getSedeOrigenId() != null && trasladoDto.getSedeDestinoId() != null
                && trasladoDto.getSedeOrigenId().equals(trasladoDto.getSedeDestinoId())) {
            throw new BadRequestException("La sede origen y destino no pueden ser iguales");
        }

        Traslado traslado = trasladoMapper.toEntity(trasladoDto);

        // Mapear custodias manualmente por el enlace bidireccional
        if (trasladoDto.getCustodias() != null && !trasladoDto.getCustodias().isEmpty()) {
            trasladoDto.getCustodias().forEach(custodiaDTO -> {
                TrasladoCustodia custodia = trasladoCustodiaMapper.toEntity(custodiaDTO);
                custodia.setTraslado(traslado); // 👈 enlace bidireccional
                traslado.getCustodias().add(custodia);
            });
        }

        return trasladoMapper.toDTO(trasladoRepository.save(traslado));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoResponseDTO> getTrasladoXEstado(Boolean estado) {
        return trasladoRepository.findByEstado(estado)
                .stream()
                .map(trasladoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TrasladoResponseDTO getTrasladoXId(Long id) {
        Traslado traslado= trasladoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Traslado no encontrado con id :"+id
        ));
        return trasladoMapper.toDTO(traslado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoResponseDTO> getTrasladoXIdInterno(Long idInterno) {
        List<Traslado> traslados = trasladoRepository.findByIdInterno(idInterno);
        if (traslados.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontraron traslados para el interno con id: " + idInterno
            );
        }
        return traslados.stream()
                .map(trasladoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TrasladoResponseDTO putTraslado(Long id, TrasladoRequestDTO trasladoDto) {
        Traslado traslado= trasladoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Traslado no encontrado con el id :"+id
                ));
        if (trasladoDto.getSedeOrigenId() !=null && trasladoDto.getSedeDestinoId()!=null
        && trasladoDto.getSedeOrigenId().equals(trasladoDto.getSedeDestinoId())){
            throw new BadRequestException("La sede de origen y destino no pueden ser iguales");
        }
        trasladoMapper.updateEntity(trasladoDto, traslado);
        if (trasladoDto.getCustodias() != null && !trasladoDto.getCustodias().isEmpty()) {
            traslado.getCustodias().clear();
            trasladoDto.getCustodias().forEach(custodiaDTO -> {
                TrasladoCustodia custodia = trasladoCustodiaMapper.toEntity(custodiaDTO);
                custodia.setTraslado(traslado);
                traslado.getCustodias().add(custodia);
            });
        }

        return trasladoMapper.toDTO(trasladoRepository.save(traslado));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoCustodiaResponseDTO> getCustodiaXId(Long idTraslado) {
        Traslado traslado = trasladoRepository.findById(idTraslado)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Traslado no encontrado con id: " + idTraslado
                ));
        return trasladoCustodiaRepository.findByTraslado(traslado)
                .stream()
                .map(trasladoCustodiaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
