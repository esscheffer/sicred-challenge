package com.erikscheffer.sicredchallenge.service;

import com.erikscheffer.sicredchallenge.exception.DuplicateVoteException;
import com.erikscheffer.sicredchallenge.model.Vote;
import com.erikscheffer.sicredchallenge.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote createVote(Vote vote) {
        Optional<Vote> existingVote =
                voteRepository.findByIdAssociateAndVoteSession(vote.getIdAssociate(), vote.getVoteSession());

        if (existingVote.isPresent()) {
            throw new DuplicateVoteException(vote.getVoteSession(), vote.getIdAssociate());
        }

        return voteRepository.save(vote);
    }
}
