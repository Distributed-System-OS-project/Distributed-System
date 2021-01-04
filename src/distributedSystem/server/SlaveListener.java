package distributedSystem.server;

import distributedSystem.Job;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

public class SlaveListener extends Thread {

	private ServerSocket serverSocket;
	private ArrayList<SlaveHandler> slaves;
	private Queue<Job> completedJobs;

	public SlaveListener(ServerSocket serverSocket, ArrayList<SlaveHandler> slaves, Queue<Job> completedJobs) {
		this.serverSocket = serverSocket;
		this.slaves = slaves;
		this.completedJobs = completedJobs;
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

				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

				System.out.println("Input streams for slave established.");

				char optimizedTask = in.readLine().charAt(0);
				System.out.println("recieved optimized task from slave: " + optimizedTask); //remove later

				SlaveHandler slave = new SlaveHandler(in, out, optimizedTask, completedJobs);
				slaves.add(slave);

				System.out.println("Added slave to slaves list");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
