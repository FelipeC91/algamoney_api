package com.myportifolio.algamoneyapi.repository;

import com.myportifolio.algamoneyapi.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByNome(String nome);
}
