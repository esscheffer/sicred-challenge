package com.erikscheffer.sicredchallenge.resource;

import com.erikscheffer.sicredchallenge.model.Vote;
import com.erikscheffer.sicredchallenge.model.apimodel.VoteApi;
import com.erikscheffer.sicredchallenge.service.VoteSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/voteSession")
public class VoteSessionResource {

    private final VoteSessionService voteSessionService;

    public VoteSessionResource(VoteSessionService voteSessionService) {
        this.voteSessionService = voteSessionService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Vote> vote(@PathVariable Long id, @Valid @RequestBody VoteApi voteApi) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voteSessionService.vote(id, voteApi));
    }
}
