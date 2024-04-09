package br.com.emannuelmorais.livrosws.service;

import br.com.emannuelmorais.livrosws.dto.request.CadastrarLivroRequest;
import br.com.emannuelmorais.livrosws.dto.response.CadastrarLivroResponse;
import br.com.emannuelmorais.livrosws.exception.ApiBusinessException;
import br.com.emannuelmorais.livrosws.model.Livro;
import br.com.emannuelmorais.livrosws.repository.LivrosRepository;
import br.com.emannuelmorais.livrosws.utils.Diretorios;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
public class LivroService {

    private final LivrosRepository livrosRepository;
    private final Diretorios diretorios;

    public CadastrarLivroResponse cadastrarLivro(CadastrarLivroRequest livroRequest) throws IOException, ParseException {
        String pathCapa = uploadCapa(livroRequest.getIsbn(), livroRequest.getCapaLivro());
        String pathPdf = uploadPdf(livroRequest.getIsbn(), livroRequest.getPdfLivro());
        Date data = new SimpleDateFormat("yyyy-MM-dd").parse(livroRequest.getDataPublicacao());

        Livro livro = livrosRepository.save(new Livro(livroRequest, pathCapa, pathPdf, data));
        return new CadastrarLivroResponse("Livro Cadastrado", livro);
    }

    public Livro findLivroByIsbn(String isbn) {
        return livrosRepository.findByIsbn(isbn);
    }

    public String uploadPdf(String isbnLivro, MultipartFile arquivo) throws IOException {
        String fileName = "pdf_livro_" + isbnLivro + ".pdf";
        return salvarArquivo(diretorios.getPathPdf(), fileName, arquivo);
    }
    public String uploadCapa(String isbnLivro, MultipartFile arquivo) throws IOException {

        String fileName = "capa_livro_" + isbnLivro + ".jpg";
        return salvarArquivo(diretorios.getPathImage(), fileName, arquivo);
    }
    @Transactional
    public Resource downloadPdf(String isbn) throws MalformedURLException {
        Livro  livro = findLivroByIsbn(isbn);
        if (livro == null)
            throw new ApiBusinessException(String.format("livro não encontrado. ISBN: %s", isbn));

        Path path = Paths.get(livro.getPdfLivro());

        if( !Files.exists(path))
            throw new ApiBusinessException(String.format("arquivo não encontrado. no Caminho:  %s", livro.getPdfLivro()));

        return new UrlResource(path.toUri());

    }


    /*PRIVATE BLOCK*/
    private String salvarArquivo(String diretorio, String nomeArquivo, MultipartFile arquivo) throws IOException {
        Path caminhoDiretorio = Paths.get(diretorio);
        if (!Files.exists(caminhoDiretorio)) {
            Files.createDirectories(caminhoDiretorio);
        }
        try (var inputStream = arquivo.getInputStream()) {
            Path caminhoArquivo = caminhoDiretorio.resolve(nomeArquivo);
            Files.copy(inputStream, caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);
        }
        return diretorio + nomeArquivo;
    }

}
