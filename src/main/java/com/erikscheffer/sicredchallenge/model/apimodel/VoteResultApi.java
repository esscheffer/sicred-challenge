package com.erikscheffer.sicredchallenge.model.apimodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteResultApi {
    private final Integer yes;
    private final Integer no;
}
