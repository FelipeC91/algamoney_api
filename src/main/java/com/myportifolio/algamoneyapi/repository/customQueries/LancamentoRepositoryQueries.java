package com.myportifolio.algamoneyapi.repository.customQueries;

import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterRecord;

import java.util.List;

public interface LancamentoRepositoryQueries {

    public List<Lancamento> filter(LancamentoFilterRecord lancamentoFilter);
}
