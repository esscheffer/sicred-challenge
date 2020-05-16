package com.erikscheffer.sicredchallenge;

import com.erikscheffer.sicredchallenge.exception.DuplicateVoteException;
import com.erikscheffer.sicredchallenge.model.Vote;
import com.erikscheffer.sicredchallenge.model.VoteSession;
import com.erikscheffer.sicredchallenge.repository.VoteRepository;
import com.erikscheffer.sicredchallenge.service.VoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteServiceTests {
    @MockBean
    VoteRepository voteRepository;

    @Autowired
    VoteService voteService;

    @Test
    public void expectVoteCreationSuccessfully() {
        VoteSession voteSession = VoteSession.builder()
                .id(5L)
                .build();
        Vote newVote = Vote.builder()
                .vote(true)
                .idAssociate(10L)
                .voteSession(voteSession)
                .build();
        Vote createdVote = Vote.builder()
                .id(1L)
                .vote(true)
                .idAssociate(10L)
                .voteSession(voteSession)
                .build();

        when(voteRepository.findByIdAssociateAndVoteSession(any(Long.class), any(VoteSession.class)))
                .thenReturn(Optional.empty());
        when(voteRepository.save(newVote)).thenReturn(createdVote);

        assertThat(voteService.createVote(newVote)).isEqualToComparingFieldByField(createdVote);
    }

    @Test
    public void expectExceptionOnDuplicateVote() {
        VoteSession voteSession = VoteSession.builder()
                .id(5L)
                .build();
        Vote existingVote = Vote.builder()
                .id(1L)
                .vote(true)
                .idAssociate(10L)
                .voteSession(voteSession)
                .build();

        when(voteRepository.findByIdAssociateAndVoteSession(10L, voteSession))
                .thenReturn(Optional.of(existingVote));

        assertThatExceptionOfType(DuplicateVoteException.class)
                .isThrownBy(() -> voteService.createVote(existingVote))
                .withMessageContaining(voteSession.getId().toString())
                .withMessageContaining(existingVote.getIdAssociate().toString());
    }
}
