package distributedSystem.server;

import distributedSystem.Job;

import java.io.BufferedReader;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

public class WorkerHandler {

    static int minimumID;
    final int workerID;
    final char optimizedTask;

    Queue<Job> waitingJobs;
    WorkerCommunicationThread comm;
    int timeToComplete;

    Queue<Job> completedJobs;


    public WorkerHandler(BufferedReader in, ObjectOutputStream out, char optimizedTask, Queue<Job> completedJobs) {
        workerID = minimumID++;
        this.optimizedTask = optimizedTask;
        waitingJobs = new LinkedList<>();
        comm = new WorkerCommunicationThread(in, out, waitingJobs, completedJobs, this);
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
        System.out.println("Job number " + job.getJobID() + " has been completed.\n");
    }

    public int getWorkerID() {
        return this.workerID;
    }

    public char getOptimizedTask() {
        return this.optimizedTask;
    }

    public int getTimeToComplete() {
        return this.timeToComplete;
    }

}
