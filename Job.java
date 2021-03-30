package ProjectManagement;

import java.util.ArrayList;

public class Job implements Comparable<Job>,JobReport_ {
	
	Project obj;
	String name;
	User obj1;
	String status;
	int t;
	int end_time;
	int arr_time;
	public int count;
	static private int c=0;
	int Waittime;
	int prior =0;
	Job(Project o,User o1){
		this.obj=o;
		this.obj1=o1;
		this.status="REQUESTED";
		this.t=t;
		this.end_time=0;
//		this.prior = obj.priority;
		count=c++;
	}
    @Override
    public int compareTo(Job job) {
    	int st = prior-job.prior;
    	if(st==0) {
	    	int st1 = obj.priority-job.obj.priority;
	    	if(st1==0) {
	    		if(count==job.count) {
	    			return 0;
	    		}
	    		else if(count<job.count) {
	    			return 1;
	    		}
	    		else {
	    			return -1;
	    		}
	    	}
	    	return (st1);
    	}
        return st;
    }
    public String toString() {
    	return "Job{id="+count +",user=" +"'"+obj1.username+"'" +", project="+"'"+obj.name+"'"+", jobstatus=" +status+ ", execution_time="+ t+", end_time="+end_time+", priority="+ prior+", name="+"'"+name+"'"+"}" ;
    	
    }
	@Override
	public String user() {
		return obj1.username; 
	}
	@Override
	public String project_name() {
		
		return obj.name;
	}
	@Override
	public int budget() {
		
		return obj.B;
	}
	@Override
	public int arrival_time() {
		
		return arr_time;
	}
	@Override
	public int completion_time() {
		
		return arr_time+t;
	}
}