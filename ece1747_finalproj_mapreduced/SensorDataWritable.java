import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class SensorDataWritable implements Writable {
	private IntWritable timestamp, sensorType;
	private ArrayWritable sensorValues;
	
	public SensorDataWritable() {
		this.timestamp = new IntWritable();
		this.sensorType = new IntWritable();
		this.sensorValues = new ArrayWritable(FloatWritable.class);
	}
	
	public SensorDataWritable(int timestamp, int sensorType, Float[] sensorValues) {
		this.timestamp = new IntWritable(timestamp);
		this.sensorType = new IntWritable(sensorType);
		FloatWritable[] tmp = new FloatWritable[sensorValues.length];
		int i=0;
		for(float sensorValue : sensorValues) {
			tmp[i] = new FloatWritable(sensorValue);
			i++;
		}
		this.sensorValues = new ArrayWritable(FloatWritable.class);
		this.sensorValues.set(tmp);
	}
	
	public void readFields(DataInput in) throws IOException {
		timestamp.readFields(in);
		sensorType.readFields(in);
		sensorValues.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		timestamp.write(out);
		sensorType.write(out);
		sensorValues.write(out);
	}
}

