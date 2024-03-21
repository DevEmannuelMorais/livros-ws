package br.com.emannuelmorais.livrosws.dto.request;

import br.com.emannuelmorais.livrosws.model.Livro;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CadastrarLivroRequest {

    private MultipartFile capaLivro;
    private MultipartFile pdfLivro;
    private String isbn;
    private String nome;
    private String autor;
    private String editora;
    private String ano;
    private String sinopse;
    private String idioma;
    private String dataPublicacao;;
    private String tipoLivro;
    private Double precoLivro;
    private Integer qtdPaginas;
    private Integer qtdEstoque;
}
