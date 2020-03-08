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
        Gauge gaugeTemp1 = Gauge.builder("temp.value",this, MeasurementService::getTempBasement).tag("location","basement")
                .register(meterRegistry);


        Gauge gaugeTemp2 = Gauge.builder("temp.value",this, MeasurementService::getTempAttic).tag("location","attic")
                .register(meterRegistry);

        Gauge gaugeHum1 = Gauge.builder("humidity.value",this, MeasurementService::getHumitidyBasement).tag("location","basement")
                .register(meterRegistry);


        Gauge gaugeHum2 = Gauge.builder("humidity.value",this, MeasurementService::getHumidityAttic).tag("location","attic")
                .register(meterRegistry);


    }

    private double randHumidity() {
        Random rand = new Random();
        int base = 50 + rand.nextInt(2);
        int decimal = rand.nextInt(9);
        return Double.parseDouble(base + "." + decimal);
    }

    private double randTemp() {
        Random rand = new Random();
        int base = 19 + rand.nextInt(2);
        int decimal = rand.nextInt(9);
        return Double.parseDouble(base + "." + decimal);
    }

    public double getHumidityAttic() {
        return randHumidity();
    }

    public double getHumitidyBasement() {
        return randHumidity();
    }

    public double getTempAttic() {
        return randTemp();
    }

    public double getTempBasement() {
        return randTemp();
    }
}
