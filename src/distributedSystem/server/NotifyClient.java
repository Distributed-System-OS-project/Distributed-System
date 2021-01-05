package distributedSystem.server;

import distributedSystem.Job;

import java.util.ArrayList;
import java.util.Queue;

public class NotifyClient extends Thread {

	ArrayList<ClientHandler> clients;
	final Queue<Job> completedJobs;

	public NotifyClient(ArrayList<ClientHandler> clients, Queue<Job> completedJobs) {
		this.clients = clients;
		this.completedJobs = completedJobs;
	}

	public void run() {
		Job nextJob;
		while (true) {
			if (! completedJobs.isEmpty()) {
				System.out.println("Completed jobs:\n" + completedJobs); //remove later
				synchronized (completedJobs) {
					nextJob = completedJobs.remove();
				}
				for (ClientHandler client : clients) {
					if (nextJob.getClientID() == client.clientID) {
						client.writeToClient("Job number " + nextJob.getJobID() + " has been completed.");
						System.out.println("Job number " + nextJob.getJobID() + " has been completed.");
					}
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
