import java.io.Serializable;

public class SensorData implements Serializable {
	private static final long serialVersionUID = 1L;

	private Float[] sensorValues;
	private int timestamp;
	private int sensorType;
	
	public Float[] getSensorValues() {
		return sensorValues;
	}
	
	public void setSensorValues(Float[] sensorValues) {
		this.sensorValues = sensorValues;
	}
	
	public int getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getSensorType() {
		return sensorType;
	}
	
	public void setSensorType(int sensorType) {
		this.sensorType = sensorType;
	}
}
