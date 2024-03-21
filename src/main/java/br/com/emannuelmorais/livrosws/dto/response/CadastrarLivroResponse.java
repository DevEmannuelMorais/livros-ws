package br.com.emannuelmorais.livrosws.dto.response;

import br.com.emannuelmorais.livrosws.model.Livro;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CadastrarLivroResponse {

    private String message;
    private Livro livro;


}
