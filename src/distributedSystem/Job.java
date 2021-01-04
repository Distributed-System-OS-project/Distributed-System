package distributedSystem;

import java.io.Serializable;
import java.util.Random;

public class Job implements Serializable {
	private int jobID;
	private String description;
	private char optimizedTask;
	private int clientID;

	private static int IDnum = 0;

	public Job(String description, int clientID) {
		this.jobID = ++IDnum;
		this.description = description;

		Random r = new Random();
		int num = r.nextInt(2);

		if (num == 0)
			this.optimizedTask = 'A';
		if (num == 1)
			this.optimizedTask = 'B';

		this.clientID = clientID;
	}

	public int getJobID() {
		return jobID;
	}

	public String getDescription() {
		return description;
	}

	public char getOptimizedTask() {
		return optimizedTask;
	}

	public int getClientID() {
		return this.clientID;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Job ID: " + getJobID());
		str.append("\nDescription: " + getDescription());
		str.append("\nOptimized Task: " + getOptimizedTask());
		str.append("\nClient ID: " + getClientID());
		return str.toString();
	}
}
	
	