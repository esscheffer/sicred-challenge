package com.erikscheffer.sicredchallenge.service;

import com.erikscheffer.sicredchallenge.model.Agenda;
import com.erikscheffer.sicredchallenge.repository.AgendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public List<Agenda> getAll() {
        return agendaRepository.findAll();
    }
}
