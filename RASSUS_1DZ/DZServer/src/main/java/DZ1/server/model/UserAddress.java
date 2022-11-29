package DZ1.server.model;

public class UserAddress {
	private String ipAddress;
	private int port;
	public UserAddress() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserAddress(String ipAddress, int port) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
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
		return "UserAddress [ipAddress=" + ipAddress + ", port=" + port + "]";
	}
	
	
}
