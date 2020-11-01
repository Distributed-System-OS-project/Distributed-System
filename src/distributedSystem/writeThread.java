package distributedSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class writeThread extends Thread{
	
	//fields
	BufferedReader stdIn;
	PrintWriter requestWriter;
	
	public writeThread(PrintWriter requestWriter, BufferedReader stdIn) {
		this.requestWriter = requestWriter;
		this.stdIn = stdIn;
	}
	
	public void run() {
		
		try{
			System.out.print("Describe the process that you want to be executed: ");
		
			String description = stdIn.readLine();
		
			while(description != null) {
			
				System.out.println("The process was sent to the processor");
				
				Job j = new Job(description);
			
				//write to server
				requestWriter.println(j);
		
				System.out.print("Describe the process that you want to be executed: ");
				description = stdIn.readLine();
			}
		}
		catch(IOException e) {
			 System.err.println("Couldn't get I/O connection.");
		}
	}
}