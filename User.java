package ProjectManagement;

import java.util.ArrayList;

public class User implements Comparable<User>,UserReport_ {
	
	String username;
	int consum;
	public int count;
	static private int c=0;
	ArrayList<Job> arrli1 = new ArrayList<Job>();
	public int last=0;
	
	User(){
		this.username=username;
		count= c++;
	}

    @Override
    public int compareTo(User user) {
    	
    	int st1 = consum-user.consum;
    	
    	if(st1==0) {
    		if(last==user.last) {
	    		if(count==user.count) {
	    			return 0;
	    		}
	    		else if(count<user.count) {
	    			return 1;
	    		}
	    		else {
	    			return -1;
	    		}
    		}
    		else {
    			return last-user.last;
    		}
    	}
    	return (st1);
    }

	@Override
	public String user() {
		
		return username;
	}

	@Override
	public int consumed() {
		return consum;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return username;
	}
}
