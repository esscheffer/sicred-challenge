package com.erikscheffer.sicredchallenge;

import com.erikscheffer.sicredchallenge.model.Agenda;
import com.erikscheffer.sicredchallenge.repository.AgendaRepository;
import com.erikscheffer.sicredchallenge.service.AgendaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AgendaServiceTests {
    @MockBean
    AgendaRepository agendaRepository;

    @Autowired
    AgendaService agendaService;

    @Test
    public void expectEmptyList() {
        when(agendaRepository.findAll()).thenReturn(new ArrayList<>());

        assertThat(agendaService.getAll()).isEmpty();
    }

    @Test
    public void expectCorrectList() {
        List<Agenda> agendas = new ArrayList<>();
        agendas.add(new Agenda(1L, "agenda 1"));
        agendas.add(new Agenda(2L, "agenda 2"));

        when(agendaRepository.findAll()).thenReturn(agendas);

        assertThat(agendaService.getAll()).containsAll(agendas);
    }

    @Test
    public void expectSaveAgenda() {
        Agenda newAgenda = new Agenda();
        newAgenda.setName("agenda 1");

        Agenda createdAgenda = new Agenda(1L, newAgenda.getName());

        when(agendaRepository.save(newAgenda)).thenReturn(createdAgenda);

        assertThat(agendaService.createAgenda(newAgenda)).isEqualToComparingFieldByField(createdAgenda);
    }

    @Test
    public void expectFindAgenda() {
        Agenda existingAgenda = new Agenda(1L, "agenda 1");

        when(agendaRepository.findById(existingAgenda.getId())).thenReturn(Optional.of(existingAgenda));

        assertThat(agendaService.findAgendaById(existingAgenda.getId())).isEqualToComparingFieldByField(existingAgenda);
    }

    @Test
    public void expectExceptionOnNotFoundAgenda() {
        Long searchAgendaId = 1L;
        when(agendaRepository.findById(searchAgendaId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> agendaService.findAgendaById(searchAgendaId))
                .withMessage("Incorrect result size: expected 1, actual 0");
    }
}
