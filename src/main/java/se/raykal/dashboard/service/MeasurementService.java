package se.raykal.dashboard.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service
public class MeasurementService {


    @Autowired
    MeterRegistry meterRegistry;

    @PostConstruct
    private void init() {
        Gauge gauge = Gauge.builder("temp.value",this, MeasurementService::getTemp).register(meterRegistry);
    }

    public double getTemp() {
        Random rand = new Random();
        int base = 19 + rand.nextInt(2);
        int decimal = rand.nextInt(9);
        return Double.parseDouble(base + "." + decimal);
    }
}
