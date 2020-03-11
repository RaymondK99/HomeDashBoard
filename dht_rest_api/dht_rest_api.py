import Adafruit_DHT
import json
import bottle
from bottle import request, response
from bottle import get


## Init temp sensor
DHT_SENSOR = Adafruit_DHT.DHT22
DHT_PIN = 4


@get('/dht22')
def listing_handler():
    '''Handles name listing'''

    # Fetch data
    humidity, temperature = Adafruit_DHT.read_retry(DHT_SENSOR, DHT_PIN)
    response.headers['Content-Type'] = 'application/json'
    response.headers['Cache-Control'] = 'no-cache'
    return json.dumps({'temperature': '%0.1f' % temperature, 'humidity': '%.1f' % humidity})


app = application = bottle.default_app()

if __name__ == '__main__':
    bottle.run(host = 'localhost', port = 8000)
