package com.erikscheffer.sicredchallenge.exception;

import com.erikscheffer.sicredchallenge.model.VoteSession;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateVoteException extends RuntimeException {
    private final VoteSession voteSession;
    private final Long idAssociate;
}
