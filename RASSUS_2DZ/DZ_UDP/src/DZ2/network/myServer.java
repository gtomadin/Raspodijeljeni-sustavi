package DZ2.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class myServer {
		
	int port;

//	public myServer(int port) throws Exception {
//		super();
//		this.port = port;
//		
//		initServer();
//	}
	
	public static void initServer(int port) throws Exception {
		
		System.out.println("Server start");

		byte[] rcvBuf = new byte[265];
		byte[] sendBuf = new byte[265];
		
		String rcvStr;
		
		DatagramSocket socket = new SimpleSimulatedDatagramSocket(port, 0.2, 200);
		
		while(true) {
			//TimeUnit.SECONDS.sleep(2);
			
			DatagramPacket packet = new DatagramPacket(rcvBuf, rcvBuf.length);
			
			socket.receive(packet);
			
			
			rcvStr = new String(packet.getData(), packet.getOffset(), packet.getLength());
			
			System.out.println("Server received: " + rcvStr);
			
			sendBuf = rcvStr.toUpperCase().getBytes();
			
			System.out.println("Server sends: " + rcvStr.toUpperCase());
			
			DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, packet.getAddress(), packet.getPort());
			
			socket.send(sendPacket);
			
			
		}
	}
}
