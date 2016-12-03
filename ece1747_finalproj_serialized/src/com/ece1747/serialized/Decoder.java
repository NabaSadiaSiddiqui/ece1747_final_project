package com.ece1747.serialized;

import java.io.*;


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
	private static String parseGenericLine(String Line)
	{
		// Check if it contains string.
		if (Line.indexOf("Notify,0x") == -1)
			return null;
		
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
			int timestampaccel = (sensorData[13] & 0xFF) 
		            | ((sensorData[14] & 0xFF) << 8) 
		            | ((sensorData[15] & 0xFF) << 16) 
		            | ((sensorData[16] & 0xFF) << 24);
			
			System.out.print("Accel:" + timestampaccel + " " + Float.intBitsToFloat(accelx) + " " + Float.intBitsToFloat(accely) + " " + Float.intBitsToFloat(accelz) + "\n"); 
			
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
			int timestampgyro = (sensorData[13] & 0xFF) 
		            | ((sensorData[14] & 0xFF) << 8) 
		            | ((sensorData[15] & 0xFF) << 16) 
		            | ((sensorData[16] & 0xFF) << 24);
			
			
			System.out.print("Gyro: " + timestampgyro + " " + Float.intBitsToFloat(gyrox) + " " + Float.intBitsToFloat(gyroy) + " " + Float.intBitsToFloat(gyroz) + "\n");  
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
			int timestamp = (sensorData[13] & 0xFF) 
		            | ((sensorData[14] & 0xFF) << 8) 
		            | ((sensorData[15] & 0xFF) << 16) 
		            | ((sensorData[16] & 0xFF) << 24);
			
			
			System.out.print("Rollx Pitchy Yawz: " + timestamp + " " + Float.intBitsToFloat(rollx) + " " + Float.intBitsToFloat(pitchy) + " " + Float.intBitsToFloat(yawz) + "\n");  
		}
	       
		return  null;
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
		try (BufferedReader br = new BufferedReader(fileReader))
		{
			// Read line by line.
			String line;
			
			// Burn the first 20 lines since they dont contain valid data
			//			for (int i = 0; i < 20; i++)
			//	br.readLine();
			
			// Start reading the valid lines.
			while ((line = br.readLine()) != null)
			{
				String sensorString;
				// Valid line.
				if ((sensorString = parseGenericLine(line)) != null)
				{
					// This line has valid data.
					System.out.print(sensorString);
				}
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.print("Exception: " + e.toString() + "\n");
		}
	}
}
