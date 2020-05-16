package com.erikscheffer.sicredchallenge.exception;

import com.erikscheffer.sicredchallenge.model.VoteSession;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmptyVoteSession extends RuntimeException {
    private final VoteSession voteSession;

    @Override
    public String getMessage() {
        return "Vote session " + voteSession.getId() + " closed.";
    }
}
