package se.raykal.dashboard.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

@Service
public class MeasurementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);

    @Autowired
    MeterRegistry meterRegistry;

    Map<String, Function> sensorMap = new HashMap();

    @Value("${random_values:false}")
    private boolean randomValues;

    @Value("${dht22_file_path:/tmp_host/dht22.out}")
    private String dhtFilePath;

    private static interface  Function {
        double getValue();
    }

    @PostConstruct
    private void init() {
        Gauge gaugeTemp1 = Gauge.builder("temp.value",this, MeasurementService::getTempBasement).tag("location","basement")
                .register(meterRegistry);


        //Gauge gaugeTemp2 = Gauge.builder("temp.value",this, MeasurementService::getTempAttic).tag("location","attic")
        //        .register(meterRegistry);

        Gauge gaugeHum1 = Gauge.builder("humidity.value",this, MeasurementService::getHumitidyBasement).tag("location","basement")
                .register(meterRegistry);


       // Gauge gaugeHum2 = Gauge.builder("humidity.value",this, MeasurementService::getHumidityAttic).tag("location","attic")
       //         .register(meterRegistry);


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
        if (randomValues) return randHumidity();

        double[] values =  readFile();
        return values[1];
    }

    public double getTempAttic() {
        return randTemp();
    }

    public double getTempBasement() {
        if (randomValues) return randTemp();

        double[] values =  readFile();
        return values[0];
    }


    private double[] readFile() {
        File myObj = new File(this.dhtFilePath);

        if (myObj.exists()) {
            try {
                Scanner scanner = new Scanner(myObj);
                double temp = scanner.nextDouble();
                double humidity =  scanner.nextDouble();
                long timeStamp = scanner.nextLong();

                LOGGER.info("Read values from file: temp={}, humidity={}, timestamp={}", temp, humidity, timeStamp);

                return new double[] {temp, humidity};
            } catch (IOException exc) {
                LOGGER.error("Unable to open file:" + dhtFilePath + ", error:" + dhtFilePath);
            }
        } else {
            LOGGER.error("Unable to locate file:" + dhtFilePath);
        }

        return new double[] {0,0};
    }
}
