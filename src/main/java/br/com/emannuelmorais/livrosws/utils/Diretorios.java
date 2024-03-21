package br.com.emannuelmorais.livrosws.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:diretorios.properties")
public class Diretorios {

    @Value("${path.pdf}")
    private String pathPdf;

    @Value("${path.image}")
    private String pathImage;
}
