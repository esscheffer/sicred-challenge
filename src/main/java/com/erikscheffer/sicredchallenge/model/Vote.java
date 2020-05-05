package com.erikscheffer.sicredchallenge.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "vote")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Boolean vote;

    private Long idAssociate;

    @ManyToOne
    @JoinColumn(name = "id_session")
    private VoteSession voteSession;
}
