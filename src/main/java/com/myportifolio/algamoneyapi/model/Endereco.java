package com.myportifolio.algamoneyapi.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Endereco {

    private String logradouro;

    private String numero;

    private String complemento;

    private String bairro;

    private String Cep;

    private String cidade;

    private String estado;
}
