package DZ1.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import DZ1.server.model.Measurement;
import DZ1.server.model.SensorDescription;
import DZ1.server.model.UserAddress;

@Service
public class ServerService {
	// @Service class
	// in this class are defined all the operation that the rest controller uses
	
	private Map<String, SensorDescription> descriptions = new HashMap<String, SensorDescription>();  // map that stores all the registered sensors
	private List<Measurement> measurements = new ArrayList<Measurement>(); // list that contains all the stored measurements 
	
	// method that register new sensors, return true if the sensor is registered  
	public boolean register(SensorDescription sensorDescription) {
		// controlling if the sensor is present in the map 
		for(String user : descriptions.keySet()){
			if(user.equals(sensorDescription.getUsername())) {
				return false;
			}
		}
		
		descriptions.put(sensorDescription.getUsername(), sensorDescription); // adding the new sensor in the map
		
		// printing all the sensors
		System.out.println("Sensors: " + descriptions.size());
		for(String user : descriptions.keySet()) {
			System.out.println(descriptions.get(user).toString());
		}
		return true;
	}
	
	// method that returns the User address of the other nearest sensor
	public UserAddress searchNeighbour(String username) {
		
		double minDistance = 0;
		SensorDescription minDistanceSensor = null;
		
		// if there is  only one sensor registered return null 
		if(descriptions.size() < 2) {
			return null;
		}
		
		SensorDescription thisSensor = descriptions.get(username);
		
		// finding the nearest sensor
		for(String user : descriptions.keySet()) {
			SensorDescription newSensor  = descriptions.get(user);
			if(!user.equals(username)) {
				double distance = calcDistance(thisSensor, newSensor);
				System.out.println("Distance between: " + thisSensor.getUsername() + " and " + newSensor.getUsername() + " = " + distance);
				if(distance < minDistance || minDistance == 0) {
					minDistance = distance;
					minDistanceSensor = newSensor;
				}
			}
		}
//		System.out.println("mindis: " + minDistance + " minsensor : " + minDistanceSensor);
		
		// returning the user address of the nearest sensor
		if(minDistanceSensor != null) {
			return new UserAddress(minDistanceSensor.getIpAddress(), minDistanceSensor.getPort());
		}
		return null;
		
	}
	
	// method that calculates the distance between two sensors
	private double calcDistance(SensorDescription sensor1, SensorDescription sensor2) {

		double R = 6371;
		double dlon = sensor2.getLongitude() - sensor1.getLongitude();
		double dlat = sensor2.getLatitude() - sensor1.getLatitude();

		
		double sinlat = Math.pow(Math.sin((dlat/2)), 2);
		double sinlon = Math.pow(Math.sin((dlon/2)), 2);
		double coscos = Math.cos(sensor1.getLatitude()) * Math.cos(sensor2.getLatitude());

		double a = sinlat + coscos * sinlon;

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c;
	}
	
	// method that stores the measurement 
	public void storeMeasurement(Measurement measurement) {
		if(measurement != null) {
			measurements.add(measurement);
		}
		// printing all the measurements
		System.out.println("Measurements:" + measurements.size());
		for(Measurement me : measurements) {	
			System.out.println(me.toString());
		}
	}
}
