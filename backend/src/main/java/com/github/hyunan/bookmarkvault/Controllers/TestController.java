package com.github.hyunan.bookmarkvault.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    public TestController() {}

    @GetMapping("/hello")
    public String sayHello() {
        return "trying this again";
    }

    @GetMapping("/bye")
    public List<Integer> sayBye() {
        return List.of(20, 23781, 123123);
    }
}
