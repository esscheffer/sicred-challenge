package com.erikscheffer.sicredchallenge;

import com.erikscheffer.sicredchallenge.exception.ClosedVoteSessionException;
import com.erikscheffer.sicredchallenge.exception.EmptyVoteSession;
import com.erikscheffer.sicredchallenge.model.Agenda;
import com.erikscheffer.sicredchallenge.model.Vote;
import com.erikscheffer.sicredchallenge.model.VoteSession;
import com.erikscheffer.sicredchallenge.model.apimodel.StartVoteSessionApi;
import com.erikscheffer.sicredchallenge.model.apimodel.VoteApi;
import com.erikscheffer.sicredchallenge.model.apimodel.VoteResultApi;
import com.erikscheffer.sicredchallenge.repository.VoteRepository;
import com.erikscheffer.sicredchallenge.repository.VoteSessionRepository;
import com.erikscheffer.sicredchallenge.service.AgendaService;
import com.erikscheffer.sicredchallenge.service.VoteService;
import com.erikscheffer.sicredchallenge.service.VoteSessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteSessionServiceTests {
    @Autowired
    VoteSessionService voteSessionService;

    @MockBean
    VoteSessionRepository voteSessionRepository;
    @MockBean
    VoteRepository voteRepository;
    @MockBean
    AgendaService agendaService;
    @MockBean
    VoteService voteService;

    @Test
    public void expectCreateVoteSession() {
        Long agendaId = 1L;
        StartVoteSessionApi startVoteSessionApi = new StartVoteSessionApi();
        Agenda agenda = new Agenda(agendaId, "new agenda");

        VoteSession createdVoteSession = VoteSession.builder()
                .id(1L)
                .durationMinutes(startVoteSessionApi.getDuration())
                .sessionStart(LocalDateTime.now())
                .agenda(agenda)
                .build();

        when(voteSessionRepository.save(any(VoteSession.class))).thenReturn(createdVoteSession);

        VoteSession voteSession = voteSessionService.createVoteSession(agendaId, startVoteSessionApi);

        assertThat(voteSession.getId()).isEqualTo(createdVoteSession.getId());
        assertThat(voteSession.getDurationMinutes()).isEqualTo(startVoteSessionApi.getDuration());
        assertThat(voteSession.getAgenda()).isEqualToComparingFieldByField(agenda);
    }

    @Test
    public void expectVoteInSessionSuccessfully() {
        Long sessionId = 1L;
        VoteApi voteApi = new VoteApi();
        voteApi.setVote(true);
        voteApi.setIdAssociate(5L);

        VoteSession voteSession = VoteSession.builder()
                .id(sessionId)
                .durationMinutes(10)
                .sessionStart(LocalDateTime.now().minusMinutes(5))
                .agenda(new Agenda(1L, "agenda 1"))
                .build();

        Vote newVote = Vote.builder()
                .id(2L)
                .vote(voteApi.getVote())
                .idAssociate(voteApi.getIdAssociate())
                .voteSession(voteSession)
                .build();

        when(voteSessionRepository.findById(sessionId)).thenReturn(Optional.of(voteSession));
        when(voteService.createVote(any(Vote.class))).thenReturn(newVote);

        assertThat(voteSessionService.vote(sessionId, voteApi)).isEqualToComparingFieldByField(newVote);
    }

    @Test
    public void expectExceptionVotingNotFoundSession() {
        Long sessionId = 1L;
        VoteApi voteApi = new VoteApi();

        when(voteSessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> voteSessionService.vote(sessionId, voteApi))
                .withMessage("Incorrect result size: expected 1, actual 0");
    }

    @Test
    public void expectExceptionVotingClosedSession() {
        Long sessionId = 1L;
        VoteApi voteApi = new VoteApi();
        voteApi.setVote(true);
        voteApi.setIdAssociate(5L);

        VoteSession voteSession = VoteSession.builder()
                .id(sessionId)
                .durationMinutes(1)
                .sessionStart(LocalDateTime.now().minusMinutes(10))
                .agenda(new Agenda(1L, "agenda 1"))
                .build();

        when(voteSessionRepository.findById(sessionId)).thenReturn(Optional.of(voteSession));

        assertThatExceptionOfType(ClosedVoteSessionException.class)
                .isThrownBy(() -> voteSessionService.vote(sessionId, voteApi))
                .withMessageContaining(sessionId.toString());
    }

    @Test
    public void expectCalculateVoteResultSuccessfully() {
        Long sessionId = 6L;

        VoteSession voteSession = VoteSession.builder()
                .id(sessionId)
                .durationMinutes(10)
                .sessionStart(LocalDateTime.now().minusMinutes(5))
                .agenda(new Agenda(1L, "agenda 1"))
                .build();

        List<Vote> votes = new ArrayList<>();
        votes.add(new Vote(1L, true, 1L, voteSession));
        votes.add(new Vote(2L, false, 2L, voteSession));
        votes.add(new Vote(3L, true, 3L, voteSession));

        when(voteSessionRepository.findById(sessionId)).thenReturn(Optional.of(voteSession));
        when(voteRepository.findAllByVoteSession(voteSession)).thenReturn(votes);

        VoteResultApi voteResultApi = voteSessionService.calculateVotesResult(sessionId);

        assertThat(voteResultApi.getNo()).isEqualTo(1);
        assertThat(voteResultApi.getYes()).isEqualTo(2);
    }

    @Test
    public void expectExceptionVotesResultNotFoundSession() {
        Long sessionId = 1L;

        when(voteSessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> voteSessionService.calculateVotesResult(sessionId))
                .withMessage("Incorrect result size: expected 1, actual 0");
    }

    @Test
    public void expectExceptionOnEmptyVoteSession() {
        Long sessionId = 6L;

        VoteSession voteSession = VoteSession.builder()
                .id(sessionId)
                .durationMinutes(10)
                .sessionStart(LocalDateTime.now().minusMinutes(5))
                .agenda(new Agenda(1L, "agenda 1"))
                .build();

        when(voteSessionRepository.findById(sessionId)).thenReturn(Optional.of(voteSession));
        when(voteRepository.findAllByVoteSession(voteSession)).thenReturn(new ArrayList<>());

        assertThatExceptionOfType(EmptyVoteSession.class)
                .isThrownBy(() -> voteSessionService.calculateVotesResult(sessionId))
                .withMessageContaining(sessionId.toString());
    }
}
