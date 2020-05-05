package com.erikscheffer.sicredchallenge.service;

import com.erikscheffer.sicredchallenge.model.Agenda;
import com.erikscheffer.sicredchallenge.repository.AgendaRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public List<Agenda> getAll() {
        return agendaRepository.findAll();
    }

    public Agenda createAgenda(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public Agenda findAgendaById(Long id) {
        Optional<Agenda> agenda = agendaRepository.findById(id);
        if (!agenda.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }

        return agenda.get();
    }
}
