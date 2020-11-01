package distributedSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{
	
	public static void main(String[] args) {
		
		String hostName = "127.0.0.1";
		int portNumber = 30121;
		
		  try (
		            Socket clientSocket = new Socket(hostName, portNumber);
		            PrintWriter requestWriter = // stream to write text requests to server
		                new PrintWriter(clientSocket.getOutputStream(), true);
		            BufferedReader responseReader= // stream to read text response from server
		                new BufferedReader(
		                    new InputStreamReader(clientSocket.getInputStream())); 
				  BufferedReader stdIn = // standard input stream to get user's requests
			                new BufferedReader(
			                    new InputStreamReader(System.in));
		        ) {
			  	
			  Thread read = new readThread(responseReader);
				
				Thread write = new writeThread(requestWriter, stdIn);
				
				read.run();
				write.run(); 
				
		        }
						catch (UnknownHostException e) {
		            System.err.println("Don't know about host " + hostName);
		            System.exit(1);
		        } catch (IOException e) {
		            System.err.println("Couldn't get I/O for the connection to " +
		                hostName);
		            System.exit(1);
		        } 
	}
}

