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
            if (!completedJobs.isEmpty()) {
                synchronized (completedJobs) {
                    nextJob = completedJobs.remove();
                }
                boolean clientFound = false;
                for (ClientHandler client : clients) {
                    if (nextJob.getClientID() == client.clientID) {
                        client.writeToClient("Job number " + nextJob.getJobID() + " has been completed.");
                        System.out.println("Client has been notified that job number " + nextJob.getJobID());
                        clientFound = true;
                        break;
                    }
                }
                if (! clientFound) {
                    System.out.println("Client number " + nextJob.getClientID() + " has disconnected.");
                }
            } else {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
