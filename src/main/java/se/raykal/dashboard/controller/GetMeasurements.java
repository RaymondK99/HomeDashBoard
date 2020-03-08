package se.raykal.dashboard.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.raykal.dashboard.service.MeasurementService;

import java.util.Random;

@RestController
@RequestMapping(path = "/api")
public class GetMeasurements {


    private static final Logger LOGGER = LoggerFactory.getLogger(GetMeasurements.class);

    @Autowired
    MeasurementService measurementService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/temp/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTemperature() {

        LOGGER.debug("Fetch measurements");
        return Double.toString(measurementService.getTempAttic());
    }
}
