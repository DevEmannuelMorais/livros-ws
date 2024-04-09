package br.com.emannuelmorais.livrosws.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class BuscarPdfLivroResponse {

    @Data
    public static class BuscarPdfLivroRequest {

        @NotBlank(message = "ISBN não pode está vazio ser informado.")
        @Size(min = 13, max = 13, message = "ISBN deve ter {max} caracteres. você informou: ${validatedValue}")
        private String isbn;
    }
}
