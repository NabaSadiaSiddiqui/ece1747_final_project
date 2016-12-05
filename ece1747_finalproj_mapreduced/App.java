import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Hello world!
 *
 */
public class App {
	
	// Mapper<keyIn, valueIn, keyOut, valueOut>
	public static class TokenizerMapper extends Mapper<Object, Text, Integer, ObjectWritable>{
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			SensorData sensorData = parseGenericLine(value.toString());
			ObjectWritable objectWritable = new ObjectWritable(SensorData.class, sensorData);
			context.write(sensorData.getTimestamp(), objectWritable);
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
	}

	// Reduce<keyIn, valueIn, keyOut, valueOut>
	public static class IntSumReducer extends Reducer<Integer, ObjectWritable, Integer, ObjectWritable> {
		private GraphData accelGraph = new GraphData();
		private GraphData gyroGraph = new GraphData();
		private GraphData angularGraph = new GraphData();
		public void reduce(Integer key, Iterable<ObjectWritable> values, Context context) throws IOException, InterruptedException {
		
			for (ObjectWritable val : values) {
				SensorData sensorData = (SensorData) val.get();
				Float[] data;
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
			ObjectWritable objectWritable = new ObjectWritable(GraphData.class, accelGraph);
			context.write(key, objectWritable);
			objectWritable = new ObjectWritable(GraphData.class, gyroGraph);
			context.write(key, objectWritable);
			objectWritable = new ObjectWritable(GraphData.class, angularGraph);
			context.write(key, objectWritable);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "word count");
		job.setJarByClass(App.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
