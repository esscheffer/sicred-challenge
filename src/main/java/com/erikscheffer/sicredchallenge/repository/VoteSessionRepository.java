package com.erikscheffer.sicredchallenge.repository;

import com.erikscheffer.sicredchallenge.model.VoteSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteSessionRepository extends JpaRepository<VoteSession, Long> {
}
