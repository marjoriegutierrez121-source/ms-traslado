package pe.inpe.ms_traslado.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorMessageDTO {

    private Integer statusCode;

    private LocalDateTime dateError;

    private String message;

    private String description;

}
