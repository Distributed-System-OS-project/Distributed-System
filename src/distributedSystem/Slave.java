package distributedSystem;

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

			out.print(optimizedTask);


			while (in.available() == 0) { //not sure if correct
				job = (Job) in.readObject();
				if (job.getOptimizedTask() == optimizedTask) {
					Thread.sleep(2000);
				} else {
					Thread.sleep(10000);
				}
				out.print(1);
				job = null;
			}

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}

	}
}
