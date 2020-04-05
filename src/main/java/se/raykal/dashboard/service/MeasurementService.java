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
import java.io.IOException;
import java.util.*;

@Service
public class MeasurementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);

    @Autowired
    MeterRegistry meterRegistry;

    Map<String, Function> sensorMap = new HashMap();

    @Value("${random_values:false}")
    private boolean randomValues;

    @Value("${dht22_file_path_foundation:/tmp_host/dht22_foundation.out}")
    private String measurementsFoundation;

    @Value("${dht22_file_path_living_room:/tmp_host/dht22_living_room.out}")
    private String measurementsLivingRoom;


    private Map<String,Long> sampleSequenceNumberMap = new HashMap<>();

    private static interface  Function {
        double getValue();
    }

    @PostConstruct
    private void init() {
        final String locationFoundationTag = "foundation";
        final String locationLivingRoom = "living room";
        final String locationTagName = "location";
        Gauge gaugeTemp1 = Gauge.builder("temp.value",this, MeasurementService::getTempFoundation).tag(locationTagName, locationFoundationTag)
                .register(meterRegistry);

        
        Gauge gaugeHum1 = Gauge.builder("humidity.value",this, MeasurementService::getHumidityFoundation).tag(locationTagName, locationFoundationTag)
                .register(meterRegistry);

        Gauge gaugeTemp2 = Gauge.builder("temp.value",this, MeasurementService::getTempLivingRoom).tag(locationTagName, locationLivingRoom)
                .register(meterRegistry);


        Gauge gaugeHum2 = Gauge.builder("humidity.value",this, MeasurementService::getHumidityLivingRoom).tag(locationTagName, locationLivingRoom)
                .register(meterRegistry);

        Gauge gaugeSampleRead = Gauge.builder("sample.sequence_number",this, MeasurementService::getSampleSequenceNumberFoundation).tag(locationTagName, locationFoundationTag)
                .register(meterRegistry);


        Gauge gaugeSampleRead2 = Gauge.builder("sample.sequence_number",this, MeasurementService::getSampleSequenceNumberLivingRoom).tag(locationTagName, locationLivingRoom)
                .register(meterRegistry);



        sensorMap.put("temp-attic", () -> getTempAttic());
        sensorMap.put("temp-foundation", () -> getTempFoundation());
        sensorMap.put("humid-attic", () -> getHumidityAttic());
        sensorMap.put("humid-foundation", () -> getHumidityFoundation());
    }


    public double getTemperature(String sensorName) {

        Function function = sensorMap.get(sensorName);

        if (function == null) {
            LOGGER.warn("Sensor: " + sensorName + " is not available.");
            throw new RuntimeException("Sensor is not available.");
        }

        return function.getValue();

    }

    public long getSampleSequenceNumberLivingRoom() {
        return sampleSequenceNumberMap.get(this.measurementsLivingRoom);
    }


    public long getSampleSequenceNumberFoundation() {
        return sampleSequenceNumberMap.get(this.measurementsFoundation);
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

    public double getTempAttic() {
        return randTemp();
    }

    private double getHumidityLivingRoom() {
        if (randomValues) return randHumidity();

        double[] values =  readFile(this.measurementsLivingRoom);
        return values[1];
    }



    public double getTempLivingRoom() {
        if (randomValues) return randTemp();

        double[] values =  readFile(this.measurementsLivingRoom);
        return values[0];
    }

    private double getHumidityFoundation() {
        if (randomValues) return randHumidity();

        double[] values =  readFile(this.measurementsFoundation);
        return values[1];
    }



    public double getTempFoundation() {
        if (randomValues) return randTemp();

        double[] values =  readFile(this.measurementsFoundation);
        return values[0];
    }


    private double[] readFile(String filePath) {
        File myObj = new File(filePath);

        if (myObj.exists()) {
            try {
                Scanner scanner = new Scanner(myObj);
                double temp = scanner.nextDouble();
                double humidity =  scanner.nextDouble();
                long sampleSequenceNumber = scanner.nextLong();

                this.sampleSequenceNumberMap.put(filePath, sampleSequenceNumber);

                LOGGER.info("Read values from file {}: temp={}, humidity={}, timestamp={}", filePath, temp, humidity,
                        sampleSequenceNumber);

                return new double[] {temp, humidity};
            } catch (IOException exc) {
                LOGGER.error("Unable to open file:" + measurementsFoundation + ", error:" + measurementsFoundation);
            }
        } else {
            LOGGER.error("Unable to locate file:" + measurementsFoundation);
        }

        return new double[] {0,0};
    }
}
