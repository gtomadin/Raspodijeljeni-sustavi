package DZ2.node;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import DZ2.model.Message;
import DZ2.model.ReadingCreator;
import DZ2.network.EmulatedSystemClock;
import DZ2.network.SimpleSimulatedDatagramSocket;
import DZ2.network.myClient;


public class Node {

	ReadingCreator rc; // defining the reading creator
	int counter; // defining the counter
	EmulatedSystemClock ESC; // defining the EmulatedSystemClock
	long startTime; // defining the start time
	long RealStartTime; // defining the real start time
	Set<Message> readings; // defining the readings set
	Map<Integer, Long> scalarDiff; // defining the map of scalar time differences
	Map<Integer, Integer> vectorSynch; // defining the map of vector synchronization
	PrintWriter pw; // defining the PrintWriter
	StringBuilder sb; // defining the StringBuilder
	
	
	final static String s = "&&"; // initializing the string separator

	
	public Node(int port, int[] ports) throws Exception {
		
		rc = new ReadingCreator(); // initializing the reading creator
		counter = 0;  // initializing the counter
		ESC = new EmulatedSystemClock(); // initializing the EmulatedSystemClock
		startTime = ESC.currentTimeMillis(); // initializing the start time
		RealStartTime = System.currentTimeMillis(); // initializing the real start time
		readings = new HashSet<Message>(); // initializing the readings set
		scalarDiff = new HashMap<Integer, Long>(); // initializing the map of scalar time differences
		vectorSynch = new HashMap<Integer, Integer>(); // initializing the map of vector synchronization
		sb = new StringBuilder(); // initializing the StringBuilder
		
		sb.append(port + "\n"); // adding port number to sb
		
		// putting the the difference for this node 
		long a = RealStartTime - startTime; 
		scalarDiff.put(port, a);
		//System.out.println("scalar: " + scalarDiff);
		
		// putting 0 for every node
		for(int i = 0; i<ports.length; ++i) {
			vectorSynch.put(ports[i], 0);
		}
		
		//System.out.println(vectorSynch);
		

		
		try {
			
			// starting the UDP server of the Node
			new Thread(() -> {
				try {
					startServer(port);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}).start();
		
			// starting the UDP client for every Node in the Network
			for(int i=0;i<ports.length; ++i) {
				if(port != ports[i]) {
					
					int ClientPort = ports[i];
					new Thread(() -> {
						try {
							startClient(port, ClientPort);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}).start();
				}
			}
			
			
			// starting the thread that sort and print the readings
			new Thread(() -> {
				try {
					sortAndPrint(port);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}).start();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}




	// method that represent the UDP client
	private void startClient(int serverport, int port) throws Exception {
		System.out.println("Client start: from " + serverport + " to " + port);
			
		String sendString = serverport + s + startTime; // first message is the port and the start time of the node
		
		byte[] rcvBuf  = new byte[256]; // received bytes
		
		// determine the IP address of a host, given the host's name
		InetAddress address = InetAddress.getByName("localhost");
		
		// create a datagram socket and bind it to any available
        // port on the local host
		DatagramSocket socket = new SimpleSimulatedDatagramSocket(0.3, 1000);
		
		
		String receiveString = null; // received string
		
		while(true) {
			System.out.println("Clinet sends: ");
			
			byte[] sendBuf = sendString.getBytes(); // sent bytes
			
			System.out.println(new String(sendBuf));
			
			// create a datagram packet for sending data
			DatagramPacket packet  = new DatagramPacket(sendBuf, sendBuf.length, address, port);
			
			// send a datagram packet from this socket
			socket.send(packet);
			
			// create a datagram packet for receiving data
			DatagramPacket rcvPacket  = new DatagramPacket(rcvBuf, rcvBuf.length);
			TimeUnit.SECONDS.sleep(1);
			try {
				// receive packet
				socket.receive(rcvPacket);
				
			} catch(SocketTimeoutException e) {
				System.out.println("Retransmision: form " + serverport + " to " + port);
				continue;
			} catch (IOException ex) {
	            Logger.getLogger(myClient.class.getName()).log(Level.SEVERE, null, ex);
	        }
			
			// received string initializing
			receiveString = new String(rcvPacket.getData(), rcvPacket.getOffset(), rcvPacket.getLength());
			
			
			
	
			// end connection
			if(counter == 500) {
				List<Message> list = new ArrayList<Message>(readings); 
			    Collections.sort(list); 
			    System.out.println(list);
				System.out.println(sb);
				break;
			}
			
			// if the confirmation is received create new reading  
			if(receiveString != null) {
				
				counter++;
				System.out.println("Port: " + port + "Client received: " + receiveString + " " + counter); // received string
				
				setVector(port); // setting vector value
				sendString = getNextString(serverport); // get new reading
				getInfo(sendString); // get info from new reading
				
//			}else {
//				// if not send again the same message  
//				
//				continue;
			}
		}
		
		
		socket.close(); 
		System.out.println("Client close");
		
	}


	// method that represent the UDP server
	private void startServer(int port) throws Exception {
		System.out.println("Server start: " + port);
	
		byte[] rcvBuf = new byte[265]; // received bytes
		byte[] sendBuf = new byte[265]; // sent bytes
		
		String rcvStr; // received string
		
		 // create a UDP socket and bind it to the specified port on the local
        // host
		DatagramSocket socket = new SimpleSimulatedDatagramSocket(port, 0.3, 1000);
		
		
		while(true) {
			//TimeUnit.SECONDS.sleep(2);
			
			// create a DatagramPacket for receiving packets
			DatagramPacket packet = new DatagramPacket(rcvBuf, rcvBuf.length);
			
			// receive packet
			socket.receive(packet);
			
			 // construct a new String form received bytes
			rcvStr = new String(packet.getData(), packet.getOffset(), packet.getLength());
			
			System.out.println("Server received: " + port + " " + rcvStr);
			
			setVector(port); // setting vector value
			
			getInfo(rcvStr); // get info from new reading
			
			//readings.add(rcvStr);
			
			// encode a String into a sequence of bytes using the platform's
            // default charset
			sendBuf = "confirmation".getBytes();
			
			//System.out.println("Server sends: " + rcvStr.toUpperCase());
			
			// create a DatagramPacket for sending packets
			DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, packet.getAddress(), packet.getPort());
			
			// send packet
			socket.send(sendPacket);
			
			
		}
		
	}
	
	//method used for sorting and printing
	private void sortAndPrint(int port) throws Exception {
		
		while(true) {
			
			TimeUnit.SECONDS.sleep(5);
			
			//calculation time
			long q= (System.currentTimeMillis() - startTime)/1000;
			System.out.println(port + " time: " + q);
			
			// sorting the deleting and reading  
			List<Message> list = new ArrayList<Message>(readings); 
		    Collections.sort(list); 
		    readings.clear();
		    
		    
		    // printing all the readings and getting the average reading
		    int sum = 0;
		    int counteri = 0;
		    //long current = System.currentTimeMillis();
		    for(Message m : list) {
		    	
		    	//System.out.println("vrijeme" + current);
		    	//System.out.println("razlika: " + (current - m.getScalartime()));
		    	//System.out.println(m.toString());
		    	sb.append(m.toString2()+"\n");
		    		
		    	int value = Integer.parseInt(m.getMeasure());
		    		
		    	sum += value;
		    		
		    	++counteri;
		    	
		    }
		    
		    if(counteri != 0) {
		    
		    	//System.out.println("Average value : " + (sum/counteri));
		    	sb.append("Average value  : " + (sum/counteri)+ "\n");
		    	sb.append("\n");
		    }
		    
			//System.out.println("comunication record");
		    //System.out.println(sb);
		    
		    // printing the reading in a file
		    pw = new PrintWriter(new FileWriter(port + ".txt"));
		    pw.append(sb);
		    pw.close();
		}
		
	}

	//method used for create a new reading
	private  String getNextString(int port) {
		
		String value = rc.getReading(startTime).getCo();
		String scalarTime = ESC.currentTimeMillis() + "";
		String vector = vectorSynch.toString();
		
		return port + s + counter + s + value + s + scalarTime + s + vector;
	}

	//method used for getting the information from String value
	private  void getInfo(String rcvStr) {
		
		String[] trim = rcvStr.split(s);
	
		
		if(trim.length == 2) {
			// getting the time difference for every node
			int port = Integer.parseInt(trim[0]);
			long delta = RealStartTime - Long.parseLong(trim[1]);
			System.out.println(port + " : " + delta );
			scalarDiff.put(port, delta);
		}else {
			// getting the reading with accurate scalar time
			
			//System.out.println(rcvStr);
			//System.out.println(scalarDiff);
			
			int port = Integer.parseInt(trim[0]);
			
			long deltaTime = scalarDiff.get(port);
			long time = Long.parseLong(trim[3]);
			long newTime = time + deltaTime;
			
			rcvStr = trim[0] + s + trim[1] + s + trim[2] + s + newTime + s + trim[4];
			Message m = new Message(port, counter, trim[2], newTime, trim[4]);
			
			boolean noInside = true;
			
			for(Message mes : readings) {
				if(mes.equals(m)) {
					noInside = false;
					break;
				}
			}
			
			if(noInside) {
				readings.add(m);
			}
			
			
			//readings.add(m);
		}
	}
	
	//method used for updating the vector values
	private  void setVector(int port) {
		int value = vectorSynch.get(port);
		++value;
	
		vectorSynch.put(port, value);
	
	}





}
