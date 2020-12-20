package distributedSystem;

import java.util.ArrayList;
import java.util.Queue;

public class NotifyClient extends Thread {

	ArrayList<ClientHandler> clients;
	Queue<Job> completedJobs;

	public NotifyClient(ArrayList<ClientHandler> clients, Queue<Job> completedJobs) {
		this.clients = clients;
		this.completedJobs = completedJobs;
	}

	public void run() {
		Job nextJob;
		while (true) {
			if (!completedJobs.isEmpty()) {
				nextJob = completedJobs.remove();
				for (ClientHandler client : clients) {
					if (nextJob.getClientID() == client.clientID) {
						client.clientWriter.println("Job number " + nextJob.getJobID() + " has been completed.");
					}
				}
			}
		}
	}

}
