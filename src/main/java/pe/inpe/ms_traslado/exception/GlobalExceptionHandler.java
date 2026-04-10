package pe.inpe.ms_traslado.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.inpe.ms_traslado.dto.ErrorMessageDTO;
import pe.inpe.ms_traslado.dto.GenericResponseDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponseDTO<Object>> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ){
        ErrorMessageDTO error = ErrorMessageDTO.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .dateError(LocalDateTime.now())
                .description(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericResponseDTO.builder().error(error).build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GenericResponseDTO<Object>> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request){

        ErrorMessageDTO error = ErrorMessageDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .dateError(LocalDateTime.now())
                .description(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponseDTO.builder().error(error).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponseDTO<Object>> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ){
        ErrorMessageDTO error = ErrorMessageDTO.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error interno del servidor")
                .dateError(LocalDateTime.now())
                .description(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenericResponseDTO.builder().error(error).build());
    }
}
