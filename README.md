<P>This is a distributed system created for our Operating Systems course, MCON-234.</P>

<h3>How it works:</h3>
<hr/>
<ol>
 <li>A Server receives connections from Worker and Client programs</li>
 <li>The Server assigns an ID number to the Clients and Workers as they connect</li>
 <li>Clients send job descriptions to the server</li>
 <li>The Server assigns a job ID and puts the job on a queue to be assigned to a Worker</li>
 <li>The Server assigns the job to the Worker who will complete it in the least amount of time</li>
 <li>The Worker spends 2s completing a task it is optimized for, or 10s completing a task it isn't optimized for</li>
 <li>The Worker then informs the Server that it has completed the task</li>
 <li>The Server then informs the Client that its task has been completed</li>
</ol>
 
 <p>These programs are fully multi-threaded, and the Client and Worker programs communicate with the Server using sockets. </p>
