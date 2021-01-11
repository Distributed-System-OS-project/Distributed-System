package distributedSystem.server;

import distributedSystem.Job;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Queue;

public class WorkerListener extends Thread {

    ServerSocket serverSocket;
    static ArrayList<WorkerHandler> workers;
    static Queue<Job> completedJobs;
    static Queue<Job> readyJobs;

    public WorkerListener(ServerSocket serverSocket, ArrayList<WorkerHandler> workers, Queue<Job> completedJobs, Queue<Job> readyJobs) {
        this.serverSocket = serverSocket;
        this.workers = workers;
        this.completedJobs = completedJobs;
        this.readyJobs = readyJobs;
    }

    @Override
    public void run() {
        System.out.println("Started WorkerListener thread.");
        while (true) {

            try {
                Socket socket;
                synchronized (serverSocket) {
                    socket = serverSocket.accept();
                }
                System.out.println("Connection established with worker.");

                socket.setSoTimeout(11000);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                System.out.println("Input streams for worker established.");

                char optimizedTask = in.readLine().charAt(0);

                WorkerHandler worker = new WorkerHandler(in, out, optimizedTask, completedJobs);
                workers.add(worker);

                System.out.println("Added worker to workers list\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeWorker(WorkerHandler worker, Job jobInProgress) {
        try {
            worker.comm.in.close();
            worker.comm.out.close();
        } catch (SocketException e) {
            System.err.println("Socket already closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        synchronized (readyJobs) {
            readyJobs.add(jobInProgress);
        }
        Job job;
        while (!worker.waitingJobs.isEmpty()) {
            job = worker.waitingJobs.remove();
            synchronized (readyJobs) {
                readyJobs.add(job);
            }
        }
        workers.remove(worker);
        System.out.println("Worker number " + worker.workerID + " has disconnected.\n");
    }
}
