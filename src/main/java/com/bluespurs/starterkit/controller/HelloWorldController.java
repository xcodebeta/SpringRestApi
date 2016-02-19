package com.bluespurs.starterkit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    public static final String INTRO = "The Bluespurs Interview Starter Kit is running properly.";
    public static final Logger log = LoggerFactory.getLogger(HelloWorldController.class);

    /**
     * The index page returns a simple String message to indicate if everything is working properly.
     * The method is mapped to "/" as a GET request.
     */
    @RequestMapping("/")
    public String helloWorld() {
        log.info("Visiting index page");
        return INTRO;
    }
}
