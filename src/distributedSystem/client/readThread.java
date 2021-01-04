package distributedSystem.client;

import distributedSystem.IntegerWrapper;

import java.io.BufferedReader;
import java.io.IOException;

public class readThread extends Thread {

	//fields
	BufferedReader responseReader;

	public readThread(BufferedReader responseReader) {
		this.responseReader = responseReader;

	}

	public void run() {

		try {

			String response;

			while (true) {
				response = responseReader.readLine();
				System.out.println(response);
			}

		} catch (IOException e) {
			System.err.println("Couldn't get I/O connection in readThread.");
		}
	}
}