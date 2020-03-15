#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>
#include "lowpower.h"

#define DHTPIN 2     // Digital pin connected to the DHT sensor 
// Feather HUZZAH ESP8266 note: use pins 3, 4, 5, 12, 13 or 14 --
// Pin 15 can work but DHT must be disconnected during program upload.

// Uncomment the type of sensor in use:
#define DHTTYPE    DHT22     // DHT 22 (AM2302)


// See guide for details on sensor wiring and usage:
//   https://learn.adafruit.com/dht/overview

DHT_Unified dht(DHTPIN, DHTTYPE);

uint32_t delayMS;

unsigned int sample_number = 1;
char buffer[100];

void setup() {
  Serial.begin(9600);
  // Initialize device.
  dht.begin();
  Serial.println(F("DHTxx Unified Sensor Example"));
  // Print temperature sensor details.
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  Serial.println(F("------------------------------------"));
  Serial.println(F("Temperature Sensor"));
  Serial.print  (F("Sensor Type: ")); Serial.println(sensor.name);
  Serial.print  (F("Driver Ver:  ")); Serial.println(sensor.version);
  Serial.print  (F("Unique ID:   ")); Serial.println(sensor.sensor_id);
  Serial.print  (F("Max Value:   ")); Serial.print(sensor.max_value); Serial.println(F("°C"));
  Serial.print  (F("Min Value:   ")); Serial.print(sensor.min_value); Serial.println(F("°C"));
  Serial.print  (F("Resolution:  ")); Serial.print(sensor.resolution); Serial.println(F("°C"));
  Serial.println(F("------------------------------------"));
  // Print humidity sensor details.
  dht.humidity().getSensor(&sensor);
  Serial.println(F("Humidity Sensor"));
  Serial.print  (F("Sensor Type: ")); Serial.println(sensor.name);
  Serial.print  (F("Driver Ver:  ")); Serial.println(sensor.version);
  Serial.print  (F("Unique ID:   ")); Serial.println(sensor.sensor_id);
  Serial.print  (F("Max Value:   ")); Serial.print(sensor.max_value); Serial.println(F("%"));
  Serial.print  (F("Min Value:   ")); Serial.print(sensor.min_value); Serial.println(F("%"));
  Serial.print  (F("Resolution:  ")); Serial.print(sensor.resolution); Serial.println(F("%"));
  Serial.println(F("------------------------------------"));
  // Set delay between sensor readings based on sensor details.
  delayMS = sensor.min_delay / 1000;
}

void loop() {
  // Go in LowPower mode
  // ATmega328P, ATmega168
  //LowPower.idle(SLEEP_8S, ADC_OFF, TIMER2_OFF, TIMER1_OFF, TIMER0_OFF, 
  //              SPI_OFF, USART0_OFF, TWI_OFF);
  // Delay between measurements.
  delay(2000);
  
  // Get temperature event and print its value.
  sensors_event_t temp_event, humid_event;

  // Read temp
  dht.temperature().getEvent(&temp_event);
  bool read_temp = !isnan(temp_event.temperature);

  // Read humidity
  dht.humidity().getEvent(&humid_event);
  bool read_humid = !isnan(humid_event.relative_humidity);

  if (read_humid && read_temp) {
    // OK
    char str_temp[20], str_humid[20];
    dtostrf(temp_event.temperature,4,2,str_temp);
    dtostrf(humid_event.relative_humidity, 4,2,str_humid);
    sprintf(buffer,"%s %s %u", str_temp, str_humid, sample_number);
    Serial.println(buffer);
    sample_number++;
  } else {
    sprintf(buffer,"Unable ro read values: read_temp:%d, read_humid:%d",read_temp,read_humid);
    Serial.println(buffer);
  }
  

}
