package DZ2.node;

import java.util.concurrent.TimeUnit;

public class NodeNetwork {
	
	
	public static void main(String[] args) {
		
		int[] ports = {10001, 10002, 10003}; // ports of the nodes
		
		try {
			// creating all the nodes
			for(int i=0;i<ports.length;++i) {
				int port = ports[i];
				new Thread(()->{
					try {
						Node node = new Node(port, ports);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}).start();
				TimeUnit.MILLISECONDS.sleep(500);
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
