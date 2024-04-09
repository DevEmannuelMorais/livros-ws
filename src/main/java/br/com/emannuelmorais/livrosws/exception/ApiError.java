package br.com.emannuelmorais.livrosws.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME, visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
@Schema(description = "ApiError", title = "ApiError")
public class ApiError {

    @JsonIgnore
    private HttpStatus httpStatus;

    @Schema(description = "Código do status HTTP", example = "400")
    private int status;

    @Schema(description = "Descrição do status HTTP", example = "Bad Request")
    private String error;

    @Schema(description = "Data e hora da requisição", example = "2022-01-01T00:00:00.000Z")
    private ZonedDateTime timestamp;

    @Schema(description = "Método HTTP da requisição", example = "POST")
    private String httpMethod;

    @Schema(description = "Caminho da requisição", example = "/api/exemplo")
    private String path;

    @Schema(description = "Mensagem de erro", example = "Um ou mais campos são inválidos. Faça o preenchimento correto e tente novamente")
    private String message;

    @Schema(description = "Mensagem de depuração do erro")
    private String debugMessage;


    @Schema(description = "Lista de SubErros", implementation = ApiSubError.class, title = "ApiSubError")
    private List<ApiSubError> subErrors;


    private ApiError() {
        this.timestamp = ZonedDateTime.now();
    }

    public ApiError(HttpStatus status, String method, String path) {
        this.timestamp = ZonedDateTime.now();
        this.httpStatus = status;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.httpMethod = method;
        this.path = path;
    }

    public ApiError(HttpStatus status, String message, Throwable ex, String method, String path) {
        this(status, method, path);
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status) {
        this();
        this.httpStatus = status;
        this.status = status.value();
        this.error = status.getReasonPhrase();
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.httpStatus = status;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = "Unexpected error";
        this.debugMessage = ex.toString();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.httpStatus = status;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.debugMessage = ex.toString();
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(cv -> {
            this.addValidationError(cv.getRootBeanClass().getSimpleName(),
                    ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                    cv.getInvalidValue(), cv.getMessage());
        });
    }

    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiSubError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiSubError(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(cv.getRootBeanClass().getSimpleName(), ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(), cv.getInvalidValue(), cv.getMessage());
    }

}
