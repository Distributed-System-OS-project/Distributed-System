package distributedSystem;
import java.util.*;

public class Job 
{
	private int jobID;
	private String description;
	private char optimizedTask;
	
	private static int IDnum = 0;
	
	public Job(String description)
	{
		this.jobID = ++IDnum;
		this.description=description;
		
		Random r = new Random();
		int num = r.nextInt(1);
		
		if(num==0)
			this.optimizedTask='A';
		if(num==1)
			this.optimizedTask='B';
	}
	
	public int getJobID()
	{
		return jobID;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public char getOptimizedTask()
	{
		return optimizedTask;
	}

	@Override
	public String toString() 
	{
		StringBuilder str = new StringBuilder();
		str.append("Job ID: " + getJobID());
		str.append("\nDescription: " + getDescription());
		str.append("\nOptimized Task: " + getOptimizedTask());
		return str.toString();
	}
}
	
	