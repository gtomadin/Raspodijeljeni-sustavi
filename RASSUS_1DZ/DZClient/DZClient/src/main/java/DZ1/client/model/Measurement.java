package DZ1.client.model;

public class Measurement {
	private String username;
	private String parametar;
	private float averageValue;
	public Measurement() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Measurement(String username, String parametar, float averageValue) {
		super();
		this.username = username;
		this.parametar = parametar;
		this.averageValue = averageValue;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
