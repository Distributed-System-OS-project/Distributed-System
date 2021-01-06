package distributedSystem.server;

import distributedSystem.Job;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

public class SlaveListener extends Thread {

    ServerSocket serverSocket;
    static ArrayList<SlaveHandler> slaves;
    static Queue<Job> completedJobs;
    static Queue<Job> readyJobs;

    public SlaveListener(ServerSocket serverSocket, ArrayList<SlaveHandler> slaves, Queue<Job> completedJobs, Queue<Job> readyJobs) {
        this.serverSocket = serverSocket;
        this.slaves = slaves;
        this.completedJobs = completedJobs;
        this.readyJobs = readyJobs;
    }

    @Override
    public void run() {
        System.out.println("Started SlaveListener thread.");
        while (true) {

            try {
                Socket socket;
                synchronized (serverSocket) {
                    socket = serverSocket.accept();
                }
                System.out.println("Connection established with slave.");

                socket.setSoTimeout(11000);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                System.out.println("Input streams for slave established.");

                char optimizedTask = in.readLine().charAt(0);

                SlaveHandler slave = new SlaveHandler(in, out, optimizedTask, completedJobs);
                slaves.add(slave);

                System.out.println("Added slave to slaves list");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeSlave(SlaveHandler slave, Job jobInProgress) {
        try {
            slave.comm.in.close();
            slave.comm.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        synchronized (readyJobs) {
            readyJobs.add(jobInProgress);
        }
        Job job;
        while (!slave.waitingJobs.isEmpty()) {
            job = slave.waitingJobs.remove();
            synchronized (readyJobs) {
                readyJobs.add(job);
            }
        }
        slaves.remove(slave);
        System.out.println("Client number " + slave.slaveID + " has disconnected.");
    }
}
