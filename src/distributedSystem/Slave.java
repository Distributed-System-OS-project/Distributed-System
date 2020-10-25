package distributedSystem;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Slave {
	public static void main(String[] args) {

		char optimizedTask = args[0].toUpperCase().charAt(0);

		int portNumber = Integer.parseInt(args[1]);

		Job job;

		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), portNumber);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


			while (in.available() == 0 ) {
				job = (Job) in.readObject();
				if (job.getOptimizedTask() == optimizedTask) {
					Thread.sleep(2000);
				}
				else {
					Thread.sleep(10000);
				}
				out.print(1);
				job = null;
			}

		}
		catch (UnknownHostException e) {
			System.out.println("Host not found.");
		}
		catch (IOException e) {
			System.out.println("IOException!");
		}
		catch (ClassNotFoundException e) {
			System.out.println("Class not found.");
		}

	}
}
