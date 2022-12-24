package com.myportifolio.algamoneyapi.repository.customQueries;

import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LancamentoRepositoryQueries {

    public Page<Lancamento> filter(LancamentoFilterRecord lancamentoFilter, Pageable pageable);
}
