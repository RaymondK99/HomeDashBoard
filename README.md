# HomeDashBoard

## Steps to start the required services

# 1. Start bluetooth connection
 sudo modprobe rfcomm
 sudo rfcomm bind rfcomm0 98:D3:32:31:0A:74

# 2. Start reading from bt connection
 nohup python raspberry/bluetooth/get_values_by_bt.py

# 3. Build and start backend services
mvn install
docker-compose build
docker-compose up -d

