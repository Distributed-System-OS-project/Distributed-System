package distributedSystem;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Server {
	public static void main(String args[]) {

		try {
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

			Queue<Job> readyJobs = new LinkedList();
			Queue<Job> completedJobs = new LinkedList<>();

			ArrayList<ClientHandler> clients = new ArrayList<>();
			ArrayList<SlaveHandler> slaves = new ArrayList<>();

			Thread clientListener = new ClientListener(serverSocket, clients, readyJobs);
			Thread slaveListener = new SlaveListener(serverSocket, slaves, completedJobs);
			Thread notifyClient = new NotifyClient(clients, completedJobs);

			clientListener.start();
			slaveListener.start();
			notifyClient.start();

			while (true) {
				Job nextJob;

				if (!readyJobs.isEmpty()) {
					synchronized (readyJobs) {
						nextJob = readyJobs.remove();
					}

					char jobType = nextJob.getOptimizedTask();


					int minWaitTime = slaves.get(0).getTimeToComplete();

					if (slaves.get(0).getOptimizedTask() == jobType)
						minWaitTime += 2;
					else
						minWaitTime += 10;

					//assigns the first queue to be the shortest (for now)
					SlaveHandler minQ = slaves.get(0);

					//go thru slaves and compare wait times to find shortest queue
					for (int i = 1; i < slaves.size(); i++) {

						int waitTime = slaves.get(i).getTimeToComplete();

						if (jobType == slaves.get(i).getOptimizedTask())
							waitTime += 2;
						else
							waitTime += 10;


						if (waitTime < minWaitTime) {
							minWaitTime = waitTime;
							minQ = slaves.get(i);
						}
					}

					//at this point it found the shortest queue
					//so it will add the nextJob to the shortest queue
					minQ.addJob(nextJob);




				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}


