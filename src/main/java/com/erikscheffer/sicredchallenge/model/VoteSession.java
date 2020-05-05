package com.erikscheffer.sicredchallenge.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote_session")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VoteSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Integer durationMinutes;

    private LocalDateTime sessionStart;

    @ManyToOne
    @JoinColumn(name = "id_agenda")
    Agenda agenda;
}
