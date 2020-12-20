package distributedSystem;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {

		IntegerWrapper clientID = new IntegerWrapper();

		int portNumber = Integer.parseInt(args[0]);


		try (
				Socket clientSocket = new Socket(InetAddress.getLocalHost(), portNumber);

				ObjectOutputStream requestWriter = // stream to write text requests to server
						new ObjectOutputStream(clientSocket.getOutputStream());
				BufferedReader responseReader = // stream to read text response from server
						new BufferedReader(
								new InputStreamReader(clientSocket.getInputStream()));
				BufferedReader stdIn = // standard input stream to get user's requests
						new BufferedReader(
								new InputStreamReader(System.in));
		) {

			Thread read = new readThread(responseReader, clientID);

			Thread write = new writeThread(requestWriter, stdIn, clientID);

			read.start();
			write.start();

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection.");
			System.exit(1);
		}
	}
}

