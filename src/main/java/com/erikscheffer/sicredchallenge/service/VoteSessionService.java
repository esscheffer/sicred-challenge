package com.erikscheffer.sicredchallenge.service;

import com.erikscheffer.sicredchallenge.model.VoteSession;
import com.erikscheffer.sicredchallenge.model.apimodel.StartVoteSessionApi;
import com.erikscheffer.sicredchallenge.repository.VoteSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VoteSessionService {
    private final VoteSessionRepository voteSessionRepository;
    private final AgendaService agendaService;

    public VoteSessionService(VoteSessionRepository voteSessionRepository, AgendaService agendaService) {
        this.voteSessionRepository = voteSessionRepository;
        this.agendaService = agendaService;
    }

    public VoteSession createVoteSession(Long agendaId, StartVoteSessionApi startVoteSessionApi) {
        return voteSessionRepository.save(VoteSession.builder()
                .agenda(agendaService.findAgendaById(agendaId))
                .sessionStart(LocalDateTime.now())
                .durationMinutes(startVoteSessionApi.getDuration())
                .build());
    }
}
