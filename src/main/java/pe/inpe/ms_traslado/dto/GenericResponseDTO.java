package pe.inpe.ms_traslado.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericResponseDTO<T> {
    private T response;
    private ErrorMessageDTO error;
}