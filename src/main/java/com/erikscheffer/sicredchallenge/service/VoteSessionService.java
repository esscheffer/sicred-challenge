package com.erikscheffer.sicredchallenge.service;

import com.erikscheffer.sicredchallenge.exception.ClosedVoteSessionException;
import com.erikscheffer.sicredchallenge.model.Vote;
import com.erikscheffer.sicredchallenge.model.VoteSession;
import com.erikscheffer.sicredchallenge.model.apimodel.StartVoteSessionApi;
import com.erikscheffer.sicredchallenge.model.apimodel.VoteApi;
import com.erikscheffer.sicredchallenge.repository.VoteSessionRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoteSessionService {
    private final VoteSessionRepository voteSessionRepository;
    private final AgendaService agendaService;
    private final VoteService voteService;

    public VoteSessionService(VoteSessionRepository voteSessionRepository,
                              AgendaService agendaService,
                              VoteService voteService) {
        this.voteSessionRepository = voteSessionRepository;
        this.agendaService = agendaService;
        this.voteService = voteService;
    }

    public VoteSession createVoteSession(Long agendaId, StartVoteSessionApi startVoteSessionApi) {
        return voteSessionRepository.save(VoteSession.builder()
                .agenda(agendaService.findAgendaById(agendaId))
                .sessionStart(LocalDateTime.now())
                .durationMinutes(startVoteSessionApi.getDuration())
                .build());
    }

    public Vote vote(Long sessionId, VoteApi voteApi) {
        VoteSession voteSession = findVoteSessionById(sessionId);
        validateVoteSessionOpen(voteSession);

        Vote newVote = Vote.builder()
                .vote(voteApi.getVote())
                .idAssociate(voteApi.getIdAssociate())
                .voteSession(voteSession)
                .build();

        return voteService.createVote(newVote);
    }

    private VoteSession findVoteSessionById(Long id) {
        Optional<VoteSession> voteSession = voteSessionRepository.findById(id);
        if (!voteSession.isPresent()) {
            throw new EmptyResultDataAccessException(1);
        }

        return voteSession.get();
    }

    private void validateVoteSessionOpen(VoteSession voteSession) {
        LocalDateTime endVoteSession = voteSession.getSessionStart().plusMinutes(voteSession.getDurationMinutes());
        if (endVoteSession.isBefore(LocalDateTime.now())) {
            throw new ClosedVoteSessionException(voteSession);
        }
    }
}
