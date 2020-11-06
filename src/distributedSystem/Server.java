package distributedSystem;

public class Server {

	
	/*
	 * 1)The server gets a job and has to decide which slave to send it to.
	 * 2)There are x amount of a slaves and y amount of b slaves
	 * 3)each slave has a queue 
	 * 4)make an array list of queues of type A slave and a separate array list 
	 * 	for queues of type b slave
	 * 5)server needs to decide which to which slave it should send the job, and then 
	 * 	it'll add it to that slave's queue line
	 * 6) for-each loop through both array lists that will get the amount of 
	 * 	time that each queue will take to process
	 * 	a) goes through each job in the queue
	 * 	b) if the optim job is the same type as its slave then add 2 seconds
	 * 	c) if the optim job is not the same as its slave, add 10 seconds
	 * 	d) if total amount of time it takes for that queue is 
	 * 		less than the current minimum (outside variable), then set the 
	 * 		current minimum outside variable to this lowest number
	 *  AND set minimum index (outside variable) to this minimum index that had
	 *  	the shortest queue time
	 * 
	 */
}


