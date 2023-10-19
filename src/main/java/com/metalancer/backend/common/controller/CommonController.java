package com.metalancer.backend.common.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@Tag(name = "Common", description = "")
@RequestMapping
public class CommonController {

    @GetMapping("/")
    public String get() {
        return "get";
    }
}
