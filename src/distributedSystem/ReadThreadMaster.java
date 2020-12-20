package distributedSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

public class ReadThreadMaster extends Thread {

	//fields
	ObjectInputStream responseReader;
	LinkedList<Job> ll;

	public ReadThreadMaster(ObjectInputStream responseReader, LinkedList<Job> ll) {
		this.responseReader = responseReader;
		this.ll = ll;
	}

	public void run() {

		try {
			Job job = (Job) responseReader.readObject();

			while (job != null) {

				ll.add(job);

				job = (Job) responseReader.readObject();
			}
		} catch (IOException e) {
			System.err.println("Couldn't get I/O connection.");
		} catch (ClassNotFoundException e) {
			//handle exception
		}
	}
}