package distributedSystem;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

class ClientListener extends Thread {
	ServerSocket serverSocket = null;
	Queue<Job> readyJobs;
	ArrayList<ClientHandler> clients;
	final int MIN_CLIENT_ID = 0;
	static int clientID;

	public ClientListener(ServerSocket serverSocket, ArrayList<ClientHandler> clients, Queue<Job> readyJobs) {
		this.readyJobs = readyJobs;
		this.serverSocket = serverSocket;
		clientID = MIN_CLIENT_ID;
	}

	public void run() {


		while (true) {

			try {
				Socket socket;
				synchronized (serverSocket) {
					socket = serverSocket.accept();
				}
				System.out.println("connection Established");

				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				PrintWriter out = new PrintWriter(socket.getOutputStream());

				out.print(clientID++);

				ClientHandler client = new ClientHandler(in, out, clientID, readyJobs);


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection Error");

			}
		}

	}
}