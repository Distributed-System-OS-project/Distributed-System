package distributedSystem.server;

import distributedSystem.Job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Queue;

public class WorkerCommunicationThread extends Thread {
    BufferedReader in;
    ObjectOutputStream out;
    final Queue<Job> waitingJobs;
    final Queue<Job> completedJobs;
    WorkerHandler parentSlave;

    public WorkerCommunicationThread(BufferedReader in, ObjectOutputStream out, Queue<Job> waitingJobs, Queue<Job> completedJobs, WorkerHandler parent) {
        this.in = in;
        this.out = out;
        this.waitingJobs = waitingJobs;
        this.completedJobs = completedJobs;
        parentSlave = parent;
    }

    @Override
    public void run() {
        Job nextJob;
        while (true) {
            if (!waitingJobs.isEmpty()) {

                synchronized (waitingJobs) {
                    nextJob = waitingJobs.remove();
                }
                try {
                    out.writeObject(nextJob);
                    while (Integer.parseInt(in.readLine()) != 1) {
                        sleep(100);
                    }
                    parentSlave.completeJob(nextJob);

                } catch (IOException | NumberFormatException e) { //throws NumberFormatException when Slave gets disconnected in the middle of processing something.
                    SlaveListener.removeSlave(parentSlave, nextJob);

                } catch (InterruptedException e) {
                    e.printStackTrace();
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
