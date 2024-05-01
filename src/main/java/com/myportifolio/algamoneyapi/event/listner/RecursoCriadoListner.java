package com.myportifolio.algamoneyapi.event.listner;

import com.myportifolio.algamoneyapi.event.RecursoCriadoEvent;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class RecursoCriadoListner implements ApplicationListener<RecursoCriadoEvent> {
    @Override
    public void onApplicationEvent(RecursoCriadoEvent event) {
        var evtResourceCode = event.getCodigo();
        var evtHttpResponse = event.getHttpServletResponse();

        resolveLocationAttributeOfHeaderResponse(evtHttpResponse, evtResourceCode);
    }

    private static void resolveLocationAttributeOfHeaderResponse(HttpServletResponse response, Long evtResourceCode) {
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{codigo}")
                .buildAndExpand(evtResourceCode)
                .toUri();
        response.setHeader("Location", uri.toString());
    }
}
