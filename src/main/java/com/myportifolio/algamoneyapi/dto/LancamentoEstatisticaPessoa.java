package com.myportifolio.algamoneyapi.dto;

import com.myportifolio.algamoneyapi.model.Pessoa;
import com.myportifolio.algamoneyapi.model.TipoLancamento;

import java.math.BigDecimal;

public record LancamentoEstatisticaPessoa(
    TipoLancamento tipo,
    Pessoa pessoa,
    BigDecimal total
) {}
