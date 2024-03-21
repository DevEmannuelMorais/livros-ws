package br.com.emannuelmorais.livrosws.repository;

import br.com.emannuelmorais.livrosws.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivrosRepository extends JpaRepository<Livro, Long> {

    Livro findByIsbn(String isbn);
}
