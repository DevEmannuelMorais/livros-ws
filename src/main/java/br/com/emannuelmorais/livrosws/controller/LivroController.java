package br.com.emannuelmorais.livrosws.controller;

import br.com.emannuelmorais.livrosws.dto.request.CadastrarLivroRequest;
import br.com.emannuelmorais.livrosws.dto.response.BuscarPdfLivroResponse;
import br.com.emannuelmorais.livrosws.dto.response.CadastrarLivroResponse;
import br.com.emannuelmorais.livrosws.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    @PostMapping("/cadastrar")
    public ResponseEntity<CadastrarLivroResponse> cadastrarLivro(@ModelAttribute @Valid CadastrarLivroRequest request) throws IOException, ParseException {
       CadastrarLivroResponse response = livroService.cadastrarLivro(request);

       return ResponseEntity.ok(response);
    }

    @PostMapping("/buscar-pdf-livro")
    public ResponseEntity<Resource> buscarPdfLivro(@RequestBody @Valid BuscarPdfLivroResponse.BuscarPdfLivroRequest buscarPdfLivroRequest) throws MalformedURLException {
        Resource resource = livroService.downloadPdf(buscarPdfLivroRequest.getIsbn());


            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
    }
}
