package com.ece1747.serialized;

import java.io.*;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Decoder {

	public Decoder() {
	}
	
	/**
	 * This function detects the word "Value:" in a line and parses the line such that 
	 * only the bytes succeeding the string are placed in the return string. The bytes are 
	 * then parsed into their corresponding time and accel / gyro values.
	 * i.e: all the sensor data bytes.
	 * 
	 * @param   Line: The latest line read from the file.
	 * @return: String if line had valid data to be decoded.
	 *  		null if line did not have to be decoded. 
	 */
	private static SensorData parseGenericLine(String Line)
	{
		// Check if it contains string.
		if (Line.indexOf("Notify,0x") == -1)
			return null;
		
		SensorData sData = new SensorData();
		Float[] data = new Float[3];
		int timestamp = 0;
		
		// Create new string with Value: XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX-XX
		String subString = Line.substring(Line.lastIndexOf("Notify,0x") + 9);
		byte[] sensorData = hexStringToByteArray(subString);
		
		// Check if accel data.
		if (sensorData[0] == 1)
		{
			int accelx = (sensorData[1] & 0xFF) 
		            | ((sensorData[2] & 0xFF) << 8) 
		            | ((sensorData[3] & 0xFF) << 16) 
		            | ((sensorData[4] & 0xFF) << 24);
			int accely = (sensorData[5] & 0xFF) 
		            | ((sensorData[6] & 0xFF) << 8) 
		            | ((sensorData[7] & 0xFF) << 16) 
		            | ((sensorData[8] & 0xFF) << 24);
			int accelz = (sensorData[9] & 0xFF) 
		            | ((sensorData[10] & 0xFF) << 8) 
		            | ((sensorData[11] & 0xFF) << 16) 
		            | ((sensorData[12] & 0xFF) << 24);
			timestamp = (sensorData[13] & 0xFF) 
		            | ((sensorData[14] & 0xFF) << 8) 
		            | ((sensorData[15] & 0xFF) << 16) 
		            | ((sensorData[16] & 0xFF) << 24);
			
			//System.out.print("Accel:" + timestampaccel + " " + Float.intBitsToFloat(accelx) + " " + Float.intBitsToFloat(accely) + " " + Float.intBitsToFloat(accelz) + "\n"); 
			data[0] = Float.intBitsToFloat(accelx);
			data[1] = Float.intBitsToFloat(accely);
			data[2] = Float.intBitsToFloat(accelz);
		}
		else if (sensorData[0] == 2)	// Check if Gyro data.
		{
			int gyrox = (sensorData[1] & 0xFF) 
		            | ((sensorData[2] & 0xFF) << 8) 
		            | ((sensorData[3] & 0xFF) << 16) 
		            | ((sensorData[4] & 0xFF) << 24);
			int gyroy = (sensorData[5] & 0xFF) 
		            | ((sensorData[6] & 0xFF) << 8) 
		            | ((sensorData[7] & 0xFF) << 16) 
		            | ((sensorData[8] & 0xFF) << 24);
			int gyroz = (sensorData[9] & 0xFF) 
		            | ((sensorData[10] & 0xFF) << 8) 
		            | ((sensorData[11] & 0xFF) << 16) 
		            | ((sensorData[12] & 0xFF) << 24);
			timestamp = (sensorData[13] & 0xFF) 
		            | ((sensorData[14] & 0xFF) << 8) 
		            | ((sensorData[15] & 0xFF) << 16) 
		            | ((sensorData[16] & 0xFF) << 24);
			
			
			//System.out.print("Gyro: " + timestampgyro + " " + Float.intBitsToFloat(gyrox) + " " + Float.intBitsToFloat(gyroy) + " " + Float.intBitsToFloat(gyroz) + "\n");  
			data[0] = Float.intBitsToFloat(gyrox);
			data[1] = Float.intBitsToFloat(gyroy);
			data[2] = Float.intBitsToFloat(gyroz);
		}
		else if (sensorData[0] == 3)	// Get Angular Data.
		{
			int rollx = (sensorData[1] & 0xFF) 
		            | ((sensorData[2] & 0xFF) << 8) 
		            | ((sensorData[3] & 0xFF) << 16) 
		            | ((sensorData[4] & 0xFF) << 24);
			int pitchy = (sensorData[5] & 0xFF) 
		            | ((sensorData[6] & 0xFF) << 8) 
		            | ((sensorData[7] & 0xFF) << 16) 
		            | ((sensorData[8] & 0xFF) << 24);
			int yawz = (sensorData[9] & 0xFF) 
		            | ((sensorData[10] & 0xFF) << 8) 
		            | ((sensorData[11] & 0xFF) << 16) 
		            | ((sensorData[12] & 0xFF) << 24);
			timestamp = (sensorData[13] & 0xFF) 
		            | ((sensorData[14] & 0xFF) << 8) 
		            | ((sensorData[15] & 0xFF) << 16) 
		            | ((sensorData[16] & 0xFF) << 24);
			
			
			//System.out.print("Rollx Pitchy Yawz: " + timestamp + " " + Float.intBitsToFloat(rollx) + " " + Float.intBitsToFloat(pitchy) + " " + Float.intBitsToFloat(yawz) + "\n");  
			data[0] = Float.intBitsToFloat(rollx);
			data[1] = Float.intBitsToFloat(pitchy);
			data[2] = Float.intBitsToFloat(yawz);
		}
	    
		sData.setSensorType(sensorData[0]);
		sData.setSensorValues(data);
		sData.setTimestamp(timestamp);
		return  sData;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	/**
	 * 
	 * @param args
	 * Input file into the system containing the log file for decoding.
	 */
	public static void main(String[] args) throws IOException{
		// Check if args are valid, if not exit.
		if (args[0] == null)
		{
			System.out.print("No valid file. Exiting");
			return;
		}
		
		// Module has a valid file.
		System.out.print("Log File: " + args[0] + "\n");
		
		// Create and Start reading File Data.
		FileReader fileReader = new FileReader(args[0]);
		BufferedReader br = new BufferedReader(fileReader);
		try
		{
			// Read line by line.
			String line;
			
			// Burn the first 20 lines since they dont contain valid data
			//			for (int i = 0; i < 20; i++)
			//	br.readLine();
			
			// Start reading the valid lines.
			GraphData accelGraph = new GraphData();
			GraphData gyroGraph = new GraphData();
			GraphData angularGraph = new GraphData();
			while ((line = br.readLine()) != null)
			{
				SensorData sensorData;
				Float[] data;
				// Valid line.
				if ((sensorData = parseGenericLine(line)) != null)
				{
					// This line has valid data.
					switch(sensorData.getSensorType()) {
						case 1: // accel
							data = sensorData.getSensorValues();
							accelGraph.addSensorX(data[0]);
							accelGraph.addSensorY(data[1]);
							accelGraph.addSensorZ(data[2]);
							accelGraph.addTimestamp(sensorData.getTimestamp());
							break;
						case 2: // gyro
							data = sensorData.getSensorValues();
							gyroGraph.addSensorX(data[0]);
							gyroGraph.addSensorY(data[1]);
							gyroGraph.addSensorZ(data[2]);
							gyroGraph.addTimestamp(sensorData.getTimestamp());
							break;
						case 3: // angular
							data = sensorData.getSensorValues();
							angularGraph.addSensorX(data[0]);
							angularGraph.addSensorY(data[1]);
							angularGraph.addSensorZ(data[2]);
							angularGraph.addTimestamp(sensorData.getTimestamp());
							break;
						default:
							System.err.println("We are getting some unknown sensor data type here");
							break;
					}
				}
			}
			renderCharts(accelGraph, "Acceleration Sensor Data", "Timestamp (ms)", "Acceleration (m/s2)", "accel.jpeg");
			renderCharts(gyroGraph, "Gyroscope Sensor Data", "Timestamp (ms)", "Gyroscope (degres/second)", "gyro.jpeg");
			renderCharts(angularGraph, "Angular Sensor Data", "Timestamp (ms)", "Angular (degrees/second)", "angular.jpeg");
		}
		catch (FileNotFoundException e)
		{
			System.out.print("Exception: " + e.toString() + "\n");
		}
	}
	
	private static void renderCharts(GraphData graph, String title, String xLabel, String yLabel, String filename) throws IOException {
		JFreeChart xylineChart = ChartFactory.createXYLineChart(title, xLabel, yLabel, createDataset(graph), PlotOrientation.VERTICAL, true, true, false);
      
		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */ 
		File XYChart = new File(filename); 
		ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
	}
	
	private static XYDataset createDataset(GraphData graph) {
		ArrayList<Integer> timestamps = graph.getTimestamp();
		
		final XYSeries xValues = new XYSeries("X values");
		final XYSeries yValues = new XYSeries("Y values");
		final XYSeries zValues = new XYSeries("Z values");

		int i=0;
		for(Float value : graph.getSensorX()) {
			xValues.add(timestamps.get(i), value);
			i++;
		}
		i=0;
		for(Float value : graph.getSensorY()) {
			yValues.add(timestamps.get(i), value);
			i++;
		}
		i=0;
		for(Float value : graph.getSensorZ()) {
			zValues.add(timestamps.get(i), value);
			i++;
		}
		
		final XYSeriesCollection dataset = new XYSeriesCollection( );
		dataset.addSeries(xValues);
		dataset.addSeries(yValues);
		dataset.addSeries(zValues);
		
		return dataset;
	}
	
	private static class SensorData {
		Float[] sensorValues;
		int timestamp;
		int sensorType;
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
	
	private static class GraphData {
		ArrayList<Integer> timestamp = new ArrayList<Integer>();
		ArrayList<Float> sensorX = new ArrayList<Float>();
		ArrayList<Float> sensorY = new ArrayList<Float>();
		ArrayList<Float> sensorZ = new ArrayList<Float>();
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
}
