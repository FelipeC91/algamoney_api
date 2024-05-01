package com.myportifolio.algamoneyapi.repository;

import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.projection.LancamentoProjection;
import com.myportifolio.algamoneyapi.repository.customQueries.LancamentoRepositoryQueries;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQueries {


}
