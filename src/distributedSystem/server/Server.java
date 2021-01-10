package distributedSystem.server;

import distributedSystem.IntegerWrapper;
import distributedSystem.Job;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Server {
    public static void main(String[] args) {

        Queue<Job> readyJobs = new LinkedList<>();
        Queue<Job> completedJobs = new LinkedList<>();
        System.out.println("Initialized Job queues");

        ArrayList<ClientHandler> clients = new ArrayList<>();
        System.out.println("Initialized clients list");
        ArrayList<SlaveHandler> slaves = new ArrayList<>();
        System.out.println("Initialized slaves list");

        IntegerWrapper maxJobID = new IntegerWrapper(0);

        try {
            ServerSocket clientSocket = new ServerSocket(Integer.parseInt(args[0]));
            ServerSocket slaveSocket = new ServerSocket(Integer.parseInt(args[1]));


            Thread clientListener = new ClientListener(clientSocket, clients, readyJobs, maxJobID);
            Thread slaveListener = new SlaveListener(slaveSocket, slaves, completedJobs, readyJobs);
            Thread notifyClient = new NotifyClient(clients, completedJobs);

            clientListener.start();
            slaveListener.start();
            notifyClient.start();

            System.out.println("Started threads\n");

            Job nextJob;

            while (true) {
                if (!readyJobs.isEmpty()) {
                    synchronized (readyJobs) {
                        nextJob = readyJobs.remove();
                    }
                    System.out.println("***\nReceived job\n" + nextJob.toString());
                    System.out.println("***\n");

                    if (slaves.isEmpty()) {
                        for (ClientHandler c : clients) {
                            if (c.clientID == nextJob.getClientID()) {
                                c.writeToClient("No slaves available to take this task. Please try again later.");
                            }
                        }
                        Thread.sleep(1000);
                        continue;
                    }

                    char jobType = nextJob.getOptimizedTask();

                    int minWaitTime = slaves.get(0).getTimeToComplete();

                    if (slaves.get(0).getOptimizedTask() == jobType)
                        minWaitTime += 2;
                    else
                        minWaitTime += 10;

                    //assigns the first queue to be the shortest (for now)
                    SlaveHandler shortestQueue = slaves.get(0);

                    //go through slaves and compare wait times to find shortest queue
                    for (int i = 1; i < slaves.size(); i++) {

                        int waitTime = slaves.get(i).getTimeToComplete();

                        if (jobType == slaves.get(i).getOptimizedTask())
                            waitTime += 2;
                        else
                            waitTime += 10;


                        if (waitTime < minWaitTime) {
                            minWaitTime = waitTime;
                            shortestQueue = slaves.get(i);
                        }
                    }

                    //at this point it found the shortest queue
                    //so it will add the nextJob to the shortest queue
                    shortestQueue.addJob(nextJob);
                    System.out.println("assigned job number " + nextJob.getJobID() +
                            " to slave number " + shortestQueue.getSlaveID() + "\n--" + shortestQueue.getTimeToComplete() + "seconds to complete.\n" );

                } else {
                    Thread.sleep(100); // if this is not in place the server's other threads don't get any CPU time.
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}


