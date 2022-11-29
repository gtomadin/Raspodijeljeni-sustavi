package DZ1.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.opencsv.bean.CsvToBeanBuilder;

import DZ1.client.model.Measurement;
import DZ1.client.model.Reading;
import DZ1.client.model.SensorDescription;
import DZ1.client.model.UserAddress;


public class Sensor { // class that represent a sensor as a REST client and a TCP server
	
	SensorDescription sensorDescription; // this sensors descriptions
	private static final int PORT = 0; // port that this sensor uses: when is 0 the ServerSocket will generate a new one 
	private static double minLat = 45.75; 
	private static double maxLat = 45.85;
	private static double minLon = 15.87;
	private static double maxLon = 16.00;
	private static long startTime; // time when the sensor is activated 
	private static String SERVER_NAME = "localhost"; // address for TCP communication
	private static String baseURL = "http://localhost:8080"; // address for REST communication
	private static RestTemplateImplementation rest; // rest client class
	private static List<Reading> readings; // list of sensor readings
	private static UserAddress address; // address of the nearest sensor
	
	public static void main(String[] args) {
		
		startTime = System.currentTimeMillis();
		rest = new RestTemplateImplementation(baseURL);
		address = null;
		
		// creating a server socket
		try(ServerSocket serverSocket = new ServerSocket(PORT)){
			
			// creating the new sensor
			Random random = new Random();
			String username = "sensor: " + startTime; 
			double latitude = minLat + (maxLat - minLat) * random.nextDouble();
			double longitude = minLon + (maxLon - minLon) * random.nextDouble();
			String ipAddress = serverSocket.getInetAddress().getHostAddress();
			int port = serverSocket.getLocalPort();
			
			SensorDescription sensorDescription = new SensorDescription(username, latitude, longitude, ipAddress, port);
			System.out.println(sensorDescription.toString());
			
			// registering the sensor to the REST controller
			System.out.println(rest.register(sensorDescription));
			
			// storing all the readings 
			readings = new CsvToBeanBuilder<Reading>( 
					new FileReader("mjerenja.csv"))
					.withType(Reading.class).build().parse();
			
			
			TimeUnit.SECONDS.sleep(5);
			
			while(true) {
				// searching for other sensors
				System.out.println("Finding neighbour");
				address = rest.searchNeighbour(sensorDescription.getUsername());
				TimeUnit.SECONDS.sleep(5);
				
				if(address != null) { // if a new sensor is found stop the search
					System.out.println("Neighbour found: " + address.toString());

					break;					
				}else { // if a new sensor is not found store this sensor measurements in the REST controller 
					System.out.println("Neighbour is not found");
					TimeUnit.SECONDS.sleep(2);
					System.out.println("Sending my measurements only");
					
					// take a reading
					int index = getIndex();
					System.out.println(index);
				
					Reading myReading = readings.get(index);
					
					System.out.println(myReading);
					
					Reading nulReading = new Reading(myReading.getTemperature(), 
													myReading.getPressure(),
													myReading.getHumidity(), 
													myReading.getCo(),
													myReading.getNo2(),
													myReading.getSo2());
					
					// store the measurement
					sendMeasurement(myReading, nulReading, sensorDescription);
					
					
					TimeUnit.SECONDS.sleep(5);
				}
				
				TimeUnit.SECONDS.sleep(5);
								
			}

			// creating a TCP client using the address of the neighbour sensor
			new Thread(() -> {
				createTPCClient(address.getPort());
			}).start();
			
			// going to TCP server
			toTCPServer(sensorDescription,serverSocket);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	// method that create a TCP server
	private static void toTCPServer(SensorDescription sensorDescription, ServerSocket serverSocket) {
		// listen for a connection to be made to server socket from a client
		// accept connection and create a new active socket which communicates with the client
		try(Socket clientSocket = serverSocket.accept()) {
			
			// create a new BufferedReader from an existing InputStream
			BufferedReader inFromClient = new BufferedReader( new InputStreamReader(
					clientSocket.getInputStream()));
			
			// create a PrintWriter from an existing OutputStream
			PrintWriter outToClient = new PrintWriter(new OutputStreamWriter(
					clientSocket.getOutputStream()), true);
			
			String received;
			
			// read incoming lines of text
			while((received = inFromClient.readLine()) != null) {
				
				if(received.equals("stop")) {
					System.out.println("Connection has finished sever");
					return;
				}
				
				// printing the received information
				System.out.println("Server received: " + received);
//				System.out.println(sensorDescription.getPort());
				
				TimeUnit.SECONDS.sleep(2);
				
				// take a reading
				int index = getIndex();
//				System.out.println(index);
				Reading ServerReading = readings.get(index);
				System.out.println(ServerReading);
				
				// format the received information 
				received = received.replaceAll("[^0-9,]", "");
				received = received + ".0";
				System.out.println(received);
				String[] elements = received.split(",");

				Reading ClientReading = new Reading(elements[0], elements[1], elements[2], elements[3], elements[4], elements[5]);
				
				TimeUnit.SECONDS.sleep(2);
				
				// store the measurement
				sendMeasurement(ClientReading, ServerReading, sensorDescription);
				
				TimeUnit.SECONDS.sleep(5);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
					e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
	}
	
	// method that create a TCP client using the address of the neighbour sensor
	private static void createTPCClient(int port) {
		
		// create a client socket and connect it to the name server on the specified port number
		try(Socket clientSocket = new Socket(SERVER_NAME, port)){
			
			System.out.println("tcpclient " + port);
			
			// get the socket's output stream and open a PrintWriter on it
			PrintWriter outToServer = new PrintWriter( new OutputStreamWriter(
					clientSocket.getOutputStream()), true);
			
			// get the socket's input stream and open a BufferedReader on it
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			
			
			BufferedReader in = new  BufferedReader(new InputStreamReader(System.in));
			
			String inputString;
			
			System.out.println("Insert command: ");
			
			// reading the input 
			while ((inputString = in.readLine()) != null && inputString.length() != 0) {
				if(inputString.equals("start")) {
					
					// take a reading
					int index = getIndex();
//					System.out.println(index);
					
					// send reading to the server
					outToServer.println(readings.get(index).toString());
					TimeUnit.SECONDS.sleep(5);
				} 
				if(inputString.equals("stop")) {
					outToServer.println("stop");
					System.out.println("Connection has finished client");
					return;
				}
				
				System.out.println("Insert command: ");
			}
				
			clientSocket.close();
			TimeUnit.SECONDS.sleep(5);
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// method that stores the measurements
	private static void sendMeasurement(Reading clientReading, Reading serverReading, SensorDescription sensorDescription) throws InterruptedException {
		
		TimeUnit.SECONDS.sleep(2);
		
		// Temperature		
		float temperatureCilent = returnFloat(clientReading.getTemperature());
		float temperatureServer = returnFloat(serverReading.getTemperature());
		
		float temperatureAverage = Math.abs((temperatureCilent + temperatureServer) / 2);
		Measurement tempMeasurement = new Measurement(sensorDescription.getUsername(), "temperature", temperatureAverage);
		System.out.println(rest.storeMesurement(tempMeasurement) + " " + tempMeasurement);
		
		TimeUnit.SECONDS.sleep(2);
		
		// Pressure
		float pressureCilent = returnFloat(clientReading.getPressure());
		float pressureServer = returnFloat(serverReading.getPressure());
		
		float pressureAverage = Math.abs((pressureCilent + pressureServer) / 2);
		Measurement pressureMeasurement = new Measurement(sensorDescription.getUsername(), "pressure", pressureAverage);
		System.out.println(rest.storeMesurement(pressureMeasurement) + " " + pressureMeasurement);
		
		TimeUnit.SECONDS.sleep(2);
		
		// Humidity
		float humidityCilent = returnFloat(clientReading.getHumidity());
		float humidityServer = returnFloat(serverReading.getHumidity());
		
		float humidityAverage = Math.abs((humidityCilent + humidityServer) / 2);
		Measurement humidityMeasurement = new Measurement(sensorDescription.getUsername(), "humidity", humidityAverage);
		System.out.println(rest.storeMesurement(humidityMeasurement) + " " + humidityMeasurement);
		
		TimeUnit.SECONDS.sleep(2);
		
		// CO
		float coCilent = returnFloat(clientReading.getCo());
		float coServer = returnFloat(serverReading.getCo());
		
		float coAverage = Math.abs((coCilent + coServer) / 2);
		Measurement coMeasurement = new Measurement(sensorDescription.getUsername(), "co", coAverage);
		System.out.println(rest.storeMesurement(coMeasurement) + " " + coMeasurement);
		
		TimeUnit.SECONDS.sleep(2);
		
		//NO2
		float no2Cilent = returnFloat(clientReading.getNo2());
		float no2Server = returnFloat(serverReading.getNo2());
		
		float no2Average = Math.abs((no2Cilent + no2Server) / 2);
		Measurement no2Measurement = new Measurement(sensorDescription.getUsername(), "no2", no2Average);
		System.out.println(rest.storeMesurement(no2Measurement) + " " + no2Measurement);
		
		TimeUnit.SECONDS.sleep(2);
		
		//SO2
		float so2Cilent = returnFloat(clientReading.getSo2());
		float so2Server = returnFloat(serverReading.getSo2());
		
		float so2Average = Math.abs((so2Cilent + so2Server) / 2);
		Measurement so2Measurement = new Measurement(sensorDescription.getUsername(), "so2", so2Average);
		System.out.println(rest.storeMesurement(so2Measurement) + " " + so2Measurement);
		
		
		TimeUnit.SECONDS.sleep(2);
		
	}
	
	// method that returns the float value of a string
	private static float returnFloat(String element) {
		if(element.length() == 0) {
			return 0;
		}else {
			return Float.parseFloat(element);
		}

	}

	// method that calculate the index
	private static int getIndex() {
		long current = System.currentTimeMillis();
		long time = (current - startTime)/1000;
		int index = (int) ((time % 100) + 2);
		System.out.println("Vrijeme proteklo od aktivacije senzora: " + time + ", redni broj: " + index);
		return index;
	}

	
}
