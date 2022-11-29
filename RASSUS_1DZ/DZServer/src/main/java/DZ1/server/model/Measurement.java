package DZ1.server.model;

public class Measurement {
	private String username;
	private String parametar;
	private float averageValue;
	public Measurement() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Measurement(String usermane, String parametar, float averageValue) {
		super();
		this.username = usermane;
		this.parametar = parametar;
		this.averageValue = averageValue;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String usermane) {
		this.username = usermane;
	}
	public String getParametar() {
		return parametar;
	}
	public void setParametar(String parametar) {
		this.parametar = parametar;
	}
	public float getAverageValue() {
		return averageValue;
	}
	public void setAverageValue(float averageValue) {
		this.averageValue = averageValue;
	}
	@Override
	public String toString() {
		return "Measurement [username=" + username + ", parametar=" + parametar + ", averageValue=" + averageValue
				+ "]";
	}
	
	
}
