import serial
import time
port = serial.Serial("/dev/rfcomm0", baudrate=9600,timeout=1)

# reading and writing data from and to arduino serially.                                      
# rfcomm0 -> this could be different
while True:
	#print "--> Write '1' to arduino bluetooth device"
	#port.write(str(1))

        try:
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
