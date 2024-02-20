package study.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.DayOfWeek;

public record ResponseDateResult(@JsonProperty("dayOfTheWeek") DayOfWeek dayOfWeek) { }
