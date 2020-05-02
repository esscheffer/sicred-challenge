package com.erikscheffer.sicredchallenge.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/agenda")
public class AgendaResource {

    @GetMapping
    public Map<String, Object> test() {
        return Collections.singletonMap("message", "Hello, World");
    }
}
