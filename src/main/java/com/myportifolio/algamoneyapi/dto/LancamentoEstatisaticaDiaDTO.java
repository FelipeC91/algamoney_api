package com.myportifolio.algamoneyapi.dto;

import com.myportifolio.algamoneyapi.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoEstatisaticaDiaDTO(
        TipoLancamento tipo,
        LocalDate dia,
        BigDecimal Total
) {}
