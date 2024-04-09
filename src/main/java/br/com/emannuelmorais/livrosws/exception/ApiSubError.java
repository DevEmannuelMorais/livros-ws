package br.com.emannuelmorais.livrosws.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Schema(description = "ApiSubError", title = "ApiSubError")
public class ApiSubError {

    @Schema(description = "Objeto que contém  erros")
    private String object;

    @Schema(description = "Campo do objeto")
    private String field;

    @Schema(description = "Valor rejeitado")
    private Object rejectedValue;

    @Schema(description = "Mensagem de erro")
    private String message;

    public ApiSubError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
