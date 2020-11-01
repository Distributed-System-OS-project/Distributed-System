package distributedSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class readThread extends Thread{
	
	//fields
	BufferedReader responseReader;
	
	public readThread(BufferedReader responseReader) {
		this.responseReader = responseReader;
	}
	
	public void run() {
		
		try{
			String response = responseReader.readLine();
			
			while(response != null) {
				System.out.println(response);
				
				response = responseReader.readLine();
			}
		}
		catch(IOException e) {
			 System.err.println("Couldn't get I/O connection.");
		}
	}
}