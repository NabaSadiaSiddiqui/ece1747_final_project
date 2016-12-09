import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class App {

	// Mapper<keyIn, valueIn, keyOut, valueOut>
	public static class TokenizerMapper extends Mapper<Object, Text, IntWritable, DoubleWritable> {

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			SensorData sensorData = extractAcceleration(value.toString());
			if(sensorData != null) {
				context.write(new IntWritable(sensorData.getTimestamp()), new DoubleWritable(sensorData.getMagnitude()));
			}
		}
		
		private static SensorData extractAcceleration(String Line)
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

				sData.setTimestamp(timestamp);
				sData.setSensorValues(data);
			} else {
				sData = null;
			}
			return  sData;
		}

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
	public static class IntSumReducer extends Reducer<IntWritable, DoubleWritable, IntWritable, DoubleWritable> {
		public void reduce(IntWritable key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
		
			for (DoubleWritable acceleration : values) {
				context.write(key, acceleration);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Sensor Parser");
		job.setJarByClass(App.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(DoubleWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
