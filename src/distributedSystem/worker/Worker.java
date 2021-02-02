package distributedSystem.worker;

import distributedSystem.Job;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker {
    public static void main(String[] args) {

        char optimizedTask = args[0].toUpperCase().charAt(0);
        String hostName = args[1];
        int portNumber = Integer.parseInt(args[2]);

        Job job;

        try {
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Created I/O streams");

            out.println(optimizedTask);
            System.out.println("Sent optimized task to server");


            while (true) { //not sure if correct
                job = (Job) in.readObject();
                System.out.println("Received job number " + job.getJobID() + "\n\tOptimized Task: " + job.getOptimizedTask());
                if (job.getOptimizedTask() == optimizedTask) {
                    Thread.sleep(2000);
                } else {
                    Thread.sleep(10000);
                }
                out.println(1);
                System.out.println("Completed job number " + job.getJobID() + "\n");
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
