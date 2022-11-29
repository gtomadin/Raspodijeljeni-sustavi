package DZ1.client;

import java.io.FileReader;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;

import DZ1.client.model.Measurement;
import DZ1.client.model.Reading;
import DZ1.client.model.SensorDescription;
import DZ1.client.model.UserAddress;

public class MainTest {
	 public static void main(String[] args) {
		List<Reading> readings  = null;
		try {
			readings = new CsvToBeanBuilder<Reading>( 
					new FileReader("mjerenja.csv"))
					.withType(Reading.class).build().parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Reading r: readings) {
			System.out.println(r.toString());
		}
		String url = "http://localhost:8080";
		RestTemplateImplementation rest = new RestTemplateImplementation(url);
		runScript(rest);
	}

	private static void runScript(RestTemplateImplementation rest) {
		
		SensorDescription sn1 = new SensorDescription("1", 10, 10 , "adr1", 1);
		SensorDescription sn2 = new SensorDescription("2", 20, 20 , "adr2", 2);
		System.out.println("sn1: " + rest.register(sn1));
		System.out.println("sn2: " + rest.register(sn2));
		
		Measurement m1 = new Measurement("1", "booo", 2);
		UserAddress user = rest.searchNeighbour(sn1.getUsername());
		System.out.println(user.getIpAddress());
		System.out.println(rest.storeMesurement(m1));
		
	}
}
