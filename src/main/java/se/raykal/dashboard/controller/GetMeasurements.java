package se.raykal.dashboard.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import se.raykal.dashboard.service.MeasurementService;

import java.util.Random;

@RestController
@RequestMapping(path = "/api")
public class GetMeasurements {


    private static final Logger LOGGER = LoggerFactory.getLogger(GetMeasurements.class);


    @Autowired
    MeasurementService measurementService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/temp/{sensorName}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTemperature(@PathVariable String sensorName) {

        LOGGER.debug("Fetch temp measurements for {}", sensorName);
        return Double.toString(measurementService.getTemperature("temp-" + sensorName));
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/humidity/{sensorName}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getHumidity(@PathVariable String sensorName) {

        LOGGER.debug("Fetch humidity measurements for {}", sensorName);
        return Double.toString(measurementService.getTemperature("humid-" + sensorName));
    }
}
