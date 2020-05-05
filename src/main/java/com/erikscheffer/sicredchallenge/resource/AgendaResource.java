package com.erikscheffer.sicredchallenge.resource;

import com.erikscheffer.sicredchallenge.model.Agenda;
import com.erikscheffer.sicredchallenge.model.VoteSession;
import com.erikscheffer.sicredchallenge.model.apimodel.StartVoteSessionApi;
import com.erikscheffer.sicredchallenge.service.AgendaService;
import com.erikscheffer.sicredchallenge.service.VoteSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/agenda")
public class AgendaResource {

    private final AgendaService agendaService;
    private final VoteSessionService voteSessionService;

    public AgendaResource(AgendaService agendaService, VoteSessionService voteSessionService) {
        this.agendaService = agendaService;
        this.voteSessionService = voteSessionService;
    }

    @GetMapping
    public List<Agenda> test() {
        return agendaService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Agenda> create(@Valid @RequestBody Agenda agenda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.createAgenda(agenda));
    }

    @PostMapping("/{agendaId}/voteSession")
    public ResponseEntity<VoteSession> create(@PathVariable Long agendaId,
                                              @RequestBody(required = false) StartVoteSessionApi startVoteSessionApi) {
        // If no configuration for the sessions selected, create a new one with the default values
        if (startVoteSessionApi == null) {
            startVoteSessionApi = new StartVoteSessionApi();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(voteSessionService.createVoteSession(agendaId, startVoteSessionApi));
    }
}
