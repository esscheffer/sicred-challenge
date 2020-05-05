package com.erikscheffer.sicredchallenge.model.apimodel;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VoteApi {
    @NotNull
    private Boolean vote;

    @NotNull
    private Long idAssociate;
}
