package DZ2.model;

public class Reading {
	private String temperature;
	private String pressure;
	private String humidity;
	private String co;
	private String no2;
	private String so2;
	
	public Reading() {
		// TODO Auto-generated constructor stub
	}

	public Reading(String temperature, String pressure, String humidity, String co, String no2, String so2) {
		super();
		this.temperature = temperature;
		this.pressure = pressure;
		this.humidity = humidity;
		this.co = co;
		this.no2 = no2;
		this.so2 = so2;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getNo2() {
		return no2;
	}

	public void setNo2(String no2) {
		this.no2 = no2;
	}

	public String getSo2() {
		return so2;
	}

	public void setSo2(String so2) {
		this.so2 = so2;
	}

	@Override
	public String toString() {
		return "Reading [temperature=" + temperature + ", pressure=" + pressure + ", humidity=" + humidity + ", co="
				+ co + ", no=" + no2 + ", so=" + so2 + "]";
	}
	
	
	
	
}
