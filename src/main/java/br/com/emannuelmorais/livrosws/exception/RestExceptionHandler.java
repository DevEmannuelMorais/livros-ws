package br.com.emannuelmorais.livrosws.exception;


import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LogManager.getLogger();

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<Campo> campos = new ArrayList<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String nome = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            campos.add(new Campo(nome, message));
        }

        Problema problema = new Problema();
        problema.setStatus(status.value());
        problema.setDataHora(LocalDateTime.now());
        problema.setTitulo("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente");
        problema.setCampos(campos);

        return handleExceptionInternal(ex, problema , headers, status, request);
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        StringBuilder builder = new StringBuilder("Content-Type ");
        builder.append(ex.getContentType());
        builder.append(" não suportado. Os Content-Type suportados são: ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle Exception, handle generic Exception.class
	 *
     * @param ex the Exception
	 * @return the ApiError object
	 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = new ApiError(BAD_REQUEST);

        Class<?> requiredType = ex.getRequiredType();

        if (requiredType != null) {
            if (requiredType.isEnum()) {
                apiError.setMessage(String.format(
                        "O parâmetro '%s' de valor '%s' não foi convertido para o tipo '%s', valores possíveis: '%s'",
                        ex.getName(), ex.getValue(), requiredType.getSimpleName(),
                        Arrays.asList(requiredType.getEnumConstants())));
            } else {
                apiError.setMessage(String.format("O parâmetro '%s' de valor '%s' não foi convertido para o tipo '%s'",
                        ex.getName(), ex.getValue(), requiredType.getSimpleName()));
            }
        }
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ApiBusinessException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleApiBusinessException(ApiBusinessException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        apiError.setDebugMessage(null);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, "Internal Server Error",ex));
    }


    /* PRIVATE BLOCK */
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

}
