package distributedSystem.server;

import distributedSystem.Job;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Queue;

public class SlaveCommunicationThread extends Thread {
	private BufferedReader in;
	private ObjectOutputStream out;
	final Queue<Job> waitingJobs;
	final Queue<Job> completedJobs;
	private SlaveHandler parentSlave;

	public SlaveCommunicationThread(BufferedReader in, ObjectOutputStream out, Queue<Job> waitingJobs, Queue<Job> completedJobs, SlaveHandler parent) {
		this.in = in;
		this.out = out;
		this.waitingJobs = waitingJobs;
		this.completedJobs = completedJobs;
		parentSlave = parent;
	}

	@Override
	public void run() {
		Job nextJob;
		while (true) {
			if (! waitingJobs.isEmpty()) {

				synchronized (waitingJobs) {
					nextJob = waitingJobs.remove();
				}
				try {
					out.writeObject(nextJob);
					while (Integer.parseInt(in.readLine()) != 1) {
						sleep(100);
					}
					synchronized (completedJobs) {
						completedJobs.add(nextJob);
					}
					parentSlave.completeJob(nextJob);

				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
