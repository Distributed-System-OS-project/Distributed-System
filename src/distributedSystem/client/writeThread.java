package distributedSystem.client;

import distributedSystem.IntegerWrapper;
import distributedSystem.Job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

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

					Job j = new Job(description, clientID.getNum());
					System.out.println("Starting a new Job with description " + description + " and clientID " + clientID.getNum());

					//write to server
					requestWriter.writeObject(j);
					System.out.println("The process was sent to the processor");

					System.out.print("Describe the process that you want to be executed: ");
					description = stdIn.readLine();
				}
			}
		} catch (IOException e) {
			System.err.println("Couldn't get I/O connection in writeThread.");
		}
	}
}