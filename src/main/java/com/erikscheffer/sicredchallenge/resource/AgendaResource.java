package com.erikscheffer.sicredchallenge.resource;

import com.erikscheffer.sicredchallenge.model.Agenda;
import com.erikscheffer.sicredchallenge.service.AgendaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agenda")
public class AgendaResource {

    private final AgendaService agendaService;

    public AgendaResource(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public List<Agenda> test() {
        return agendaService.getAll();
    }
}
