package br.com.emannuelmorais.livrosws.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CadastrarLivroRequest {


    private MultipartFile capaLivro;
    private MultipartFile pdfLivro;
    @NotBlank(message = "ISBN deve ser informado. Você é informou: ${validatedValue}")
    @Size(min = 13, max = 13, message = "ISBN deve ter {max} caracteres.")
    private String isbn;
    @NotBlank(message = "Nome deve ser informado")
    private String nome;
    @NotBlank(message = "Autor deve ser informado")
    private String autor;
    @NotBlank(message = "Editora deve ser informada")
    private String editora;
    @NotBlank(message = "Ano deve ser informado")
    private String ano;
    @NotBlank(message = "Sinopse deve ser informada")
    private String sinopse;
    @NotBlank(message = "Idioma deve ser informado")
    private String idioma;
    @NotNull(message = "Data deve ser informada")
    private String dataPublicacao;
    @NotBlank(message = "Tipo deve ser informado")
    private String tipoLivro;
    @NotNull(message = "Preço deve ser informado")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private Double precoLivro;
    private Integer qtdPaginas;
    private Integer qtdEstoque;
}
