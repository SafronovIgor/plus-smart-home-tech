package ru.yandex.practicum.telemetry.collector.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/events", consumes = APPLICATION_JSON_VALUE)
public class EventController {

    public void test() {

    }
}
