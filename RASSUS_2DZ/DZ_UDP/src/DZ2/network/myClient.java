package DZ2.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;



public class myClient {
	
	
	int port;
	
	public static void initClient(int port) throws Exception{
		
		
		System.out.println("Client start");
		
		String sendString = "string";
		
		byte[] rcvBuf  = new byte[256];
		
		
		InetAddress address = InetAddress.getByName("localhost");
		
		
		DatagramSocket socket = new SimpleSimulatedDatagramSocket(0.2, 200);
		
		StringBuffer receiveString = new StringBuffer();
		
		while(true) {
			System.out.println("Clinet sends: ");
			
			byte[] sendBuf = toBytes(sendString);
			
			System.out.print(new String(sendBuf));
			
			DatagramPacket packet  = new DatagramPacket(sendBuf, sendBuf.length, address, port);
			
			socket.send(packet);
			
			System.out.println("");
			
			
			
			DatagramPacket rcvPacket  = new DatagramPacket(rcvBuf, rcvBuf.length);
			
			try {
				socket.receive(rcvPacket);
				
			} catch(SocketTimeoutException e) {
				break;
			} catch (IOException ex) {
                Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
            }
			
			receiveString.append(new String(rcvPacket.getData(), rcvPacket.getOffset(), rcvPacket.getLength()));
			
			System.out.println("client rec: " + receiveString);
			
			if(receiveString.equals(sendString.toLowerCase())) {
				
				break;
			}
			TimeUnit.SECONDS.sleep(1);
		}
		
		System.out.println("Client received: " + receiveString);
		
		
		socket.close();
		System.out.println("Client close");
	}

	private static byte[] toBytes(String sendString) {
		byte[] bytes = new byte[256];
		
		for(int i = 0; i < sendString.length(); ++i) {
			
			bytes[i] = (byte) sendString.charAt(i);
			
		}	
			
		return bytes;
	}
	
	
	
	
}
