import java.io.Serializable;

public class SensorData implements Serializable {
	private static final long serialVersionUID = 1L;

	private Float[] sensorValues;
	private int timestamp;
	
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

	public double getMagnitude() {
		double sum = Math.pow(sensorValues[0], 2) + Math.pow(sensorValues[1], 2) + Math.pow(sensorValues[2], 2);
		return Math.sqrt(sum);
	}
}
