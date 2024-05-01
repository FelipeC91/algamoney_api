package com.myportifolio.algamoneyapi.event;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class RecursoCriadoEvent extends ApplicationEvent {
    private HttpServletResponse httpServletResponse;
    private Long codigo;
    public RecursoCriadoEvent(Object source, HttpServletResponse httpServletResponse, Long codigo) {
        super(source);
        this.codigo = codigo;
        this.httpServletResponse = httpServletResponse;
    }
}
