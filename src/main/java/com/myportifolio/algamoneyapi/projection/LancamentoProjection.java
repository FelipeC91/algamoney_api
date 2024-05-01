package com.myportifolio.algamoneyapi.projection;

import com.myportifolio.algamoneyapi.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoProjection(

        Long codigo,
        String descricao,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        BigDecimal valor,
        TipoLancamento tipo,
        String categoria,
        String pessoa
){}
