package DZ2.model;

import java.util.HashMap;
import java.util.Map;


public class Message implements Comparable<Message>{
	
	
	int port;
	int counter;
	String measure;
	long scalartime;
	Map<Integer, Integer> vector;
	final static String s = "&&";
	
	public Message(int port, int counter, String measure, long scalartime, String vector) {
		super();
		this.port = port;
		this.counter = counter;
		this.measure = measure;
		this.scalartime = scalartime;
		this.vector = toMap(vector);
	}
	
	private Map<Integer, Integer> toMap(String vector2) {
		Map<Integer, Integer> help = new HashMap<Integer, Integer>();
		String[] trim = vector2.split(", ");
		
		for(int i=0;i<trim.length; ++i) {
			trim[i] = trim[i].replaceAll("[^0-9=]", "");
			String[] s = trim[i].split("=");
			help.put(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
		}
		
		return help;
		
	}

	@Override
	public String toString() {
		return port + s + counter + s + measure + s + scalartime + s + vector;
	}
	
	public String toString2() {
		return toString().replace(s, " , ");
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public long getScalartime() {
		return scalartime;
	}

	public void setScalartime(long scalartime) {
		this.scalartime = scalartime;
	}

	public Map<Integer, Integer> getVector() {
		return vector;
	}

	public void setVector(Map<Integer, Integer> vector) {
		this.vector = vector;
	}

	public static String getS() {
		return s;
	}

	@Override
	public int compareTo(Message m) {
		
		int size = vector.size();
		int counter = 0;
		
		for(int port : vector.keySet()) {
			//-help[port-10000] = vector.get(port) - m.getVector().get(port);
			int delta = vector.get(port) - m.getVector().get(port);
			if(delta >= 0) {
				counter++;
			}else {
				counter--;
			}
			
		}
		
		// 0 0 1 ,  1 1 1  
		
		// 2 0 1 , 1 0 3
		if(Math.abs(counter) == size && counter >= 0) {
			return 1;
		}
		
		if(Math.abs(counter) == size && counter < 0) {
			return -1;
		}
		
		if(Math.abs(counter) < size) {
			return (int) (scalartime - m.getScalartime());
		}
		
		
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Message) {
			Message m = (Message) obj;
			return this.toString().equals(m.toString());
		}
		return false;
	}
	
	
	
}
