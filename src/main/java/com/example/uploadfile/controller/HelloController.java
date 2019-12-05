package com.example.uploadfile.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('USER')")
    public String getHello() {
        return "Hello";
    }
}
