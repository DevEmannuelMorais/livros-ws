package br.com.emannuelmorais.livrosws.model;

import br.com.emannuelmorais.livrosws.dto.request.CadastrarLivroRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_livro", schema = "livros", catalog = "dblivros_dev")
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true, length = 13, name = "isbn")
    private String isbn;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private String editora;

    private String ano;

    @Column(nullable = false)
    private String sinopse;

    @Column(nullable = false)
    private String idioma;

    @Column(nullable = false)
    private Date dataPublicacao;

    @Column(nullable = false)
    private String pdfLivro;

    private String capaLivro;

    @Column(nullable = false)
    private String tipoLivro;

    private Double precoLivro;

    private Integer qtdPaginas;

    private Integer qtdEstoque;

    public Livro(CadastrarLivroRequest livro, String capaLivro, String pdfLivro, Date dataPublicacao) {
        this.isbn = livro.getIsbn();
        this.nome = livro.getNome();
        this.autor = livro.getAutor();
        this.editora = livro.getEditora();
        this.ano = livro.getAno();
        this.sinopse = livro.getSinopse();
        this.idioma = livro.getIdioma();
        this.dataPublicacao = dataPublicacao;
        this.pdfLivro = pdfLivro;
        this.capaLivro = capaLivro;
        this.tipoLivro = livro.getTipoLivro();
        this.precoLivro = livro.getPrecoLivro();
        this.qtdPaginas = livro.getQtdPaginas();
        this.qtdEstoque = livro.getQtdEstoque();
    }


}
