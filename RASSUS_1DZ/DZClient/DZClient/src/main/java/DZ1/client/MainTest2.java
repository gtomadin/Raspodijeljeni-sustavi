package DZ1.client;

import java.util.concurrent.TimeUnit;

public class MainTest2 {
	
	
	public static void main(String[] args) {
		
		
		
		
		
		try {
		
		
			new Thread(()->{
				Sensor.main(null);
			});
			
			TimeUnit.SECONDS.sleep(2);
			
			new Thread(()->{
				Sensor.main(null);
			});
			
			TimeUnit.SECONDS.sleep(2);
			
			new Thread(()->{
				Sensor.main(null);
			
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
