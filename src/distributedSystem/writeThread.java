package distributedSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class writeThread extends Thread {

	//fields
	BufferedReader stdIn;
	ObjectOutputStream requestWriter;
	IntegerWrapper clientID;

	public writeThread(ObjectOutputStream requestWriter, BufferedReader stdIn, IntegerWrapper clientID) {
		this.requestWriter = requestWriter;
		this.stdIn = stdIn;
		this.clientID = clientID;
	}

	public void run() {

		try {
			System.out.print("Describe the process that you want to be executed: ");

			while (true) {
				String description = stdIn.readLine();

				while (description != null) {

					System.out.println("The process was sent to the processor");

					Job j = new Job(description, clientID.anInt);

					//write to server
					requestWriter.writeObject(j);

					System.out.print("Describe the process that you want to be executed: ");
					description = stdIn.readLine();
				}
			}
		} catch (IOException e) {
			System.err.println("Couldn't get I/O connection.");
		}
	}
}