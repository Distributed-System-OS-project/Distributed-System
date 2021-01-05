package distributedSystem.server;

import distributedSystem.IntegerWrapper;
import distributedSystem.Job;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Queue;

public class ClientHandler {

	ClientCommunicationThread clientCommunicationThread;
	PrintWriter clientWriter;
	int clientID;

	public ClientHandler(ObjectInputStream in, PrintWriter out, int clientID, Queue<Job> readyJobs, IntegerWrapper maxJobID) {
		clientCommunicationThread = new ClientCommunicationThread(in, readyJobs, maxJobID);
		clientCommunicationThread.start();

		this.clientWriter = out;
		this.clientID = clientID;
	}

	public void writeToClient(String message) {
		clientWriter.println(message);
	}


}
