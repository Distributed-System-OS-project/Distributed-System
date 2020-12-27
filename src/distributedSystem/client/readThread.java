package distributedSystem.client;

import distributedSystem.IntegerWrapper;

import java.io.BufferedReader;
import java.io.IOException;

public class readThread extends Thread {

	//fields
	BufferedReader responseReader;
	IntegerWrapper clientID;

	public readThread(BufferedReader responseReader, IntegerWrapper clientID) {
		this.responseReader = responseReader;
		this.clientID = clientID;

	}

	public void run() {

		try {
			clientID.anInt = Integer.parseInt(responseReader.readLine());
			String response = responseReader.readLine();

			while (response != null) {
				System.out.println(response);

				response = responseReader.readLine();
			}
		} catch (IOException e) {
			System.err.println("Couldn't get I/O connection.");
		}
	}
}