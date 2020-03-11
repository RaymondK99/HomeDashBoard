import Adafruit_DHT
import time


## Init temp sensor
DHT_SENSOR = Adafruit_DHT.DHT22
DHT_PIN = 4



def read_loop():
    while True:
        # Fetch data
        humidity, temperature = Adafruit_DHT.read_retry(DHT_SENSOR, DHT_PIN)

        # write to file
        out_file = open("/tmp/dht22.out","w+")
        out_file.write("%.1f %.1f" % (temperature, humidity))
        out_file.close()

        # Sleep some
        time.sleep(60)


if __name__ == '__main__':
    read_loop()
