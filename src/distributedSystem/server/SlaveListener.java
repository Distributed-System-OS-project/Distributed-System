package distributedSystem.server;

import distributedSystem.Job;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
		while (true) {

			try {
				Socket socket;
				synchronized (serverSocket) {
					socket = serverSocket.accept();
				}
				DataInputStream in = new DataInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

				char optimizedTask = in.readChar();

				SlaveHandler slave = new SlaveHandler(in, out, optimizedTask, completedJobs);
				slaves.add(slave);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
