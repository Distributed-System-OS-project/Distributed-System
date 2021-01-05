package distributedSystem.server;

import distributedSystem.IntegerWrapper;
import distributedSystem.Job;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Queue;

class ClientCommunicationThread extends Thread {

	ObjectInputStream in;
	final Queue<Job> readyJobs;
	IntegerWrapper maxJobID;

	public ClientCommunicationThread(ObjectInputStream in, Queue<Job> readyJobs, IntegerWrapper maxJobID) {
		this.in = in;
		this.readyJobs = readyJobs;
		this.maxJobID = maxJobID;
	}

	public void run() {

		while (true) {
			Object object = null; //line is an object

			try {
				object = in.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}

			Job job;

			while (object != null) {
				if (object instanceof Job) {
					job = (Job) object;
					int jobID;
					synchronized (maxJobID) {
						jobID = maxJobID.getNum();
						maxJobID.setNum(jobID+1);
					}
					job.setJobID(jobID);
					System.out.println("Job received: " + job.toString());
					synchronized (readyJobs) {
						readyJobs.add(job);
					}
					System.out.println("Added job number " + job.getJobID() + " to readyJobs list");
				}
				try {
					object = in.readObject();
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
