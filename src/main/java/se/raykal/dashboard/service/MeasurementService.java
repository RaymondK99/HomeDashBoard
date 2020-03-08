package se.raykal.dashboard.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MeasurementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);

    @Autowired
    MeterRegistry meterRegistry;

    Map<String, Function> sensorMap = new HashMap();

    private static interface  Function {
        double getValue();
    }

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


        sensorMap.put("temp-attic", () -> getTempAttic());
        sensorMap.put("temp-basement", () -> getTempBasement());
        sensorMap.put("humid-attic", () -> getHumidityAttic());
        sensorMap.put("humid-basement", () -> getHumitidyBasement());
    }


    public double getTemperature(String sensorName) {

        Function function = sensorMap.get(sensorName);

        if (function == null) {
            LOGGER.warn("Sensor: " + sensorName + " is not available.");
            throw new RuntimeException("Sensor is not available.");
        }

        return function.getValue();

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

    private double getHumidityAttic() {
        return randHumidity();
    }

    private double getHumitidyBasement() {
        return randHumidity();
    }

    public double getTempAttic() {
        return randTemp();
    }

    public double getTempBasement() {
        return randTemp();
    }
}
