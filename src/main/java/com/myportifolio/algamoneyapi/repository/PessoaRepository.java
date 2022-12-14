package com.myportifolio.algamoneyapi.repository;

import com.myportifolio.algamoneyapi.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
