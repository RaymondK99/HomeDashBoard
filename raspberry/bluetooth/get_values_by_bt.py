import serial
import time
port = serial.Serial("/dev/rfcomm0", baudrate=9600,timeout=1)

# reading and writing data from and to arduino serially.                                      
# rfcomm0 -> this could be different
while True:
	time.sleep(1)

        try:
           port.write("read\n");
           rcv = port.readline()
	   if rcv:
	      print("<-- Read from Arduino:" + rcv)
              out_file = open("/tmp/dht22.out","w+")
              out_file.write(rcv)
              out_file.close()
           else:
              print("No data..")
        except serial.serialutil.SerialException as serial_exception:
           print(serial_exception)
           ## open again...
           port = serial.Serial("/dev/rfcomm0", baudrate=9600,timeout=1)
           time.sleep(1)
