package com.erikscheffer.sicredchallenge.repository;

import com.erikscheffer.sicredchallenge.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}
