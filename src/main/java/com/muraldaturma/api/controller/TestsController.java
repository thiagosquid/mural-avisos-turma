package com.muraldaturma.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/teste", "/test"})
public class TestsController {

    @GetMapping(value = "/a")
    public String test1(@RequestParam("p") Integer p) throws InterruptedException {
        if (p == 1)
            Thread.sleep(10000);

        return "test1";
    }

    @GetMapping(value = "/b")
    public String test2() throws InterruptedException {
//        Thread.sleep(10000);
        return "test2";
    }
}
