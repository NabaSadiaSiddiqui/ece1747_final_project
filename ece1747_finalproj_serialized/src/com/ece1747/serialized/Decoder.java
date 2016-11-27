
package com.ece1747.serialized;


public class Decoder {

	public Decoder() {
		
	}

	/**
	 * 
	 * @param args
	 * Input file into the system containing the log file for decoding.
	 */
	public static void main(String[] args) {
		// Check if args are valid, if not exit.
		if (args[0] == null)
		{
			System.out.print("No valid file. Exiting");
			return;
		}
		
		// Module has a valid file.
		System.out.print("Log File: " + args[0]);
	}
}