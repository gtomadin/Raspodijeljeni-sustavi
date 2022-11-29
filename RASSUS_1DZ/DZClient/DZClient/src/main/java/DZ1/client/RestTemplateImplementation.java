package DZ1.client;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import DZ1.client.model.Measurement;
import DZ1.client.model.SensorDescription;
import DZ1.client.model.UserAddress;

public class RestTemplateImplementation {
	
	// class that implements the rest client methods using RestTemplate
	
	private String baseURL;
	private RestTemplate restTemplate;
	
	public RestTemplateImplementation(String url) {
		this.baseURL = url;
		
		restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter()); // using .json mapping
	}
	
	// method that calls the register method of the REST controller 
	public ResponseEntity<String> register(SensorDescription sensorDescription) {
		ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/register", sensorDescription, String.class);
		return response;
	}
	
	// method that calls the searchNeighour method of the REST controller
	public UserAddress searchNeighbour(String username) {
		UserAddress user = restTemplate.getForObject(baseURL + "/searchNeighbour/" + username, UserAddress.class);
		return user;
	}
	
	// method that calls the storeMesurement method of the REST controller
	public ResponseEntity<String> storeMesurement(Measurement measurement) {
		ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "/storeMeasurement", measurement, String.class);
		return response;
	}
}
