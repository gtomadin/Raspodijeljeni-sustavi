package DZ1.client.model;

public class SensorDescription {
	private String username;
	private double latitude;
	private double longitude;
	private String ipAddress;
	private int port;
	public SensorDescription() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SensorDescription(String username, double latitude, double longitude, String ipAddress, int port) {
		super();
		this.username = username;
		this.latitude = latitude;
		this.longitude = longitude;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return "SensorDescription [username=" + username + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", ipAddress=" + ipAddress + ", port=" + port + "]";
	} 
	
	
}
