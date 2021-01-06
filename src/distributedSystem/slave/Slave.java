package distributedSystem.slave;

import distributedSystem.Job;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Slave {
    public static void main(String[] args) {

        char optimizedTask = args[0].toUpperCase().charAt(0);

        int portNumber = Integer.parseInt(args[1]);

        Job job;

        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Created I/O streams");

            out.println(optimizedTask);
            System.out.println("Sent optimized task to server");


            while (true) { //not sure if correct
                job = (Job) in.readObject();
                System.out.println("Received job number " + job.getJobID());
                if (job.getOptimizedTask() == optimizedTask) {
                    Thread.sleep(2000);
                } else {
                    Thread.sleep(10000);
                }
                out.println(1);
                System.out.println("Completed job number " + job.getJobID());
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
