import time


def read_loop():
    while True:
        # Fetch data
        temperature = 22.1
        humidity = 29.4

        # write to file
        out_file = open("/tmp/dht22.out","w+")
        out_file.write("%.1f %.1f %d\n" % (temperature, humidity, int(time.time())))
        out_file.close()

        # Sleep some
        time.sleep(10)


if __name__ == '__main__':
    read_loop()
