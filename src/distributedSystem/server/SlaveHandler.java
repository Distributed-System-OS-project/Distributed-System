package distributedSystem.server;

import distributedSystem.Job;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

public class SlaveHandler {

    static int minimumID;
    final int slaveID;
    final char optimizedTask;

    Queue<Job> waitingJobs;
    SlaveCommunicationThread comm;
    int timeToComplete;

    Queue<Job> completedJobs;


    public SlaveHandler(BufferedReader in, ObjectOutputStream out, char optimizedTask, Queue<Job> completedJobs) {
        slaveID = minimumID++;
        this.optimizedTask = optimizedTask;
        waitingJobs = new LinkedList<>();
        comm = new SlaveCommunicationThread(in, out, waitingJobs, completedJobs, this);
        timeToComplete = 0;
        comm.start();
        this.completedJobs = completedJobs;
    }

    public void addJob(Job job) {
        synchronized (waitingJobs) {
            waitingJobs.add(job);
        }
        if (job.getOptimizedTask() == optimizedTask) {
            timeToComplete += 2;
        } else {
            timeToComplete += 10;
        }
    }

    public void completeJob(Job job) {
        if (job.getOptimizedTask() == optimizedTask) {
            timeToComplete -= 2;
        } else {
            timeToComplete -= 10;
        }
        synchronized (completedJobs) {
            completedJobs.add(job);
        }
    }

    public int getSlaveID() {
        return this.slaveID;
    }

    public char getOptimizedTask() {
        return this.optimizedTask;
    }

    public int getTimeToComplete() {
        return this.timeToComplete;
    }

}
