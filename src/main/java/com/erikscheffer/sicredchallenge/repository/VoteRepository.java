package com.erikscheffer.sicredchallenge.repository;

import com.erikscheffer.sicredchallenge.model.Vote;
import com.erikscheffer.sicredchallenge.model.VoteSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByIdAssociateAndVoteSession(Long idAssociate, VoteSession voteSession);

    List<Vote> findAllByVoteSession(VoteSession voteSession);
}
