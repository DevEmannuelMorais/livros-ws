package br.com.emannuelmorais.livrosws.controller;

import br.com.emannuelmorais.livrosws.dto.request.CadastrarLivroRequest;
import br.com.emannuelmorais.livrosws.dto.response.BuscarPdfLivroResponse;
import br.com.emannuelmorais.livrosws.dto.response.CadastrarLivroResponse;
import br.com.emannuelmorais.livrosws.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Cadastrar um novo livro", responses = {
            @ApiResponse(responseCode = "200", description = "Livro cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CadastrarLivroResponse.class)))
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<CadastrarLivroResponse> cadastrarLivro(@ModelAttribute @Valid CadastrarLivroRequest request) throws IOException, ParseException {

        CadastrarLivroResponse response = livroService.cadastrarLivro(request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar PDF de um livro pelo ISBN", responses = {
            @ApiResponse(responseCode = "200", description = "PDF do livro encontrado e retornado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE))
    })
    @PostMapping("/buscar-pdf-livro")
    public ResponseEntity<Resource> buscarPdfLivro(@RequestBody @Valid BuscarPdfLivroResponse.BuscarPdfLivroRequest buscarPdfLivroRequest) throws MalformedURLException {
        Resource resource = livroService.downloadPdf(buscarPdfLivroRequest.getIsbn());


            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
    }
}
