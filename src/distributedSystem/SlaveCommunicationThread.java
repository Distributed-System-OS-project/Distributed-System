package distributedSystem;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Queue;

public class SlaveCommunicationThread extends Thread {
	private DataInputStream in;
	private ObjectOutputStream out;
	final Queue<Job> waitingJobs;
	final Queue<Job> completedJobs;
	private SlaveHandler parentSlave;

	public SlaveCommunicationThread(DataInputStream in, ObjectOutputStream out, Queue<Job> waitingJobs, Queue<Job> completedJobs, SlaveHandler parent) {
		this.in = in;
		this.out = out;
		this.waitingJobs = waitingJobs;
		this.completedJobs = completedJobs;
		parentSlave = parent;
	}

	@Override
	public void run() {
		while (true) {
			if (!waitingJobs.isEmpty()) {
				Job nextJob;
				synchronized (waitingJobs) {
					nextJob = waitingJobs.remove();
				}
				try {
					out.writeObject(nextJob);
					while (in.readInt() != 1) ;
					synchronized (completedJobs) {
						completedJobs.add(nextJob);
					}
					parentSlave.completeJob(nextJob);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
