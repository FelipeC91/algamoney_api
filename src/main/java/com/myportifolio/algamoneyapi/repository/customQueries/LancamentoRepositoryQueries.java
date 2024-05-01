package com.myportifolio.algamoneyapi.repository.customQueries;

import com.myportifolio.algamoneyapi.dto.LancamentoEstatisaticaDiaDTO;
import com.myportifolio.algamoneyapi.dto.LancamentoEstatisticaCategoriaDTO;
import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.projection.LancamentoProjection;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepositoryQueries {

    Page<Lancamento> filter(LancamentoFilterDTO lancamentoFilter, Pageable pageable);
    Page<LancamentoProjection> resumir(LancamentoFilterDTO lancamentoFilterDTO, Pageable pageable);

    List<LancamentoEstatisticaCategoriaDTO> listarPorCategoria(LocalDate mesReferencia );
    List<LancamentoEstatisaticaDiaDTO> listarPorDia(LocalDate mesReferencia );

}
