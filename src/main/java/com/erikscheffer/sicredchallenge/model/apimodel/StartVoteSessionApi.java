package com.erikscheffer.sicredchallenge.model.apimodel;

import lombok.Data;

@Data
public class StartVoteSessionApi {
    // Default to one minute as per documentation
    private Integer duration = 1;
}
