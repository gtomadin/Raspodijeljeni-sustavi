package DZ1.server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import DZ1.server.model.Measurement;
import DZ1.server.model.SensorDescription;
import DZ1.server.model.UserAddress;

@RestController
public class ServerController {
	// @RestController class
	// this class contains the rest controller methods
	private ServerService serverService;

	public ServerController(ServerService serverService) {
		super();
		this.serverService = serverService;
	}
	
	// rest method that uses the register method of the Service class
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody SensorDescription sensorDescription) {
		if(serverService.register(sensorDescription)) {
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	// rest method that uses the searchNeighour method of the Service class
	@GetMapping("/searchNeighbour/{username}")
	public UserAddress searchNeighour(@PathVariable("username") String username) {
		return serverService.searchNeighbour(username);
	}
	
	// rest method that uses the storeMesurement method of the Service class
	@PostMapping("/storeMeasurement")
	public ResponseEntity<?> storeMesurement(@RequestBody Measurement measurement) {
		serverService.storeMeasurement(measurement);
		
		return ResponseEntity.ok().build();
	}
}
