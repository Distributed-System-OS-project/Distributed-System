package distributedSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Queue;

class ClientCommunicationThread extends Thread {

	ObjectInputStream in;
	PrintWriter out;
	Queue<Job> readyJobs;

	public ClientCommunicationThread(ObjectInputStream in, Queue<Job> readyJobs) {
		this.in = in;
		this.readyJobs = readyJobs;
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

					readyJobs.add(job);
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
