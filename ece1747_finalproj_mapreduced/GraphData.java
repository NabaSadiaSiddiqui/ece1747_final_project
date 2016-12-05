import java.util.ArrayList;

public class GraphData {
	private ArrayList<Integer> timestamp = new ArrayList<Integer>();
	private ArrayList<Float> sensorX = new ArrayList<Float>();
	private ArrayList<Float> sensorY = new ArrayList<Float>();
	private ArrayList<Float> sensorZ = new ArrayList<Float>();
	
	public ArrayList<Integer> getTimestamp() {
		return timestamp;
	}
	
	public ArrayList<Float> getSensorX() {
		return sensorX;
	}
	
	public ArrayList<Float> getSensorY() {
		return sensorY;
	}
	
	public ArrayList<Float> getSensorZ() {
		return sensorZ;
	}
	
	public void addTimestamp(Integer timestamp) {
		this.timestamp.add(timestamp);
	}
	
	public void addSensorX(Float sensorX) {
		this.sensorX.add(sensorX);
	}
	
	public void addSensorY(Float sensorY) {
		this.sensorY.add(sensorY);
	}
	
	public void addSensorZ(Float sensorZ) {
		this.sensorZ.add(sensorZ);
	}
}
