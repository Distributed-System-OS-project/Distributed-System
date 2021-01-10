package distributedSystem.server;

import distributedSystem.IntegerWrapper;
import distributedSystem.Job;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Queue;

class ClientCommunicationThread extends Thread {

    ObjectInputStream in;
    final Queue<Job> readyJobs;
    IntegerWrapper maxJobID;
    ClientHandler parent;

    public ClientCommunicationThread(ObjectInputStream in, Queue<Job> readyJobs, IntegerWrapper maxJobID, ClientHandler parent) {
        this.in = in;
        this.readyJobs = readyJobs;
        this.maxJobID = maxJobID;
        this.parent = parent;
    }

    public void run() {

        boolean connected = true;

        Job job = null;
        do {
            try {
                job = (Job) in.readObject();
                if (job == null) {
                    connected = false;
                    ClientListener.removeClient(parent);
                    break;
                }
            } catch (EOFException e) {
                connected = false;
                ClientListener.removeClient(parent);
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.getStackTrace();
            }

            int jobID;
            synchronized (maxJobID) {
                jobID = maxJobID.getNum();
                maxJobID.setNum(jobID + 1);
            }
            job.setJobID(jobID);
            parent.writeToClient("Job: " + job.getJobID() + "\tDescription: " + job.getDescription());
            synchronized (readyJobs) {
                readyJobs.add(job);
            }


        } while (connected);

    }
}
