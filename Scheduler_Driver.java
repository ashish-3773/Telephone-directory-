package ProjectManagement;


import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import PriorityQueue.MaxHeap;
import PriorityQueue.PriorityQueueDriverCode;
import RedBlack.RBTree;
import RedBlack.RedBlackNode;
import Trie.Trie;
import Trie.TrieNode;

public class Scheduler_Driver extends Thread implements SchedulerInterface {


	public static void main(String[] args) throws IOException {
		//

		Scheduler_Driver scheduler_driver = new Scheduler_Driver();
		File file;
		if (args.length == 0) {
			URL url = Scheduler_Driver.class.getResource("INP");
			file = new File(url.getPath());
		} else {
			file = new File(args[0]);
		}

		scheduler_driver.execute(file);
	}

	public void execute(File commandFile) throws IOException {


		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(commandFile));

			String st;
			while ((st = br.readLine()) != null) {
				String[] cmd = st.split(" ");
				if (cmd.length == 0) {
					System.err.println("Error parsing: " + st);
					return;
				}
				String project_name, user_name;
				Integer start_time, end_time;

				long qstart_time, qend_time;
				//                System.out.println(st+" " +sel.currentSize);
				ArrayList<UserReport_> res;
				switch (cmd[0]) {
				case "PROJECT":
					handle_project(cmd);
					break;
				case "JOB":
					handle_job(cmd);
					break;
				case "USER":
					handle_user(cmd[1]);
					break;
				case "QUERY":
					handle_query(cmd[1]);
					break;
				case "": // HANDLE EMPTY LINE
					handle_empty_line();
					break;
				case "ADD":
					handle_add(cmd);
					break;
					//--------- New Queries
				case "NEW_PROJECT":
				case "NEW_USER":
				case "NEW_PROJECTUSER":
				case "NEW_PRIORITY":
					timed_report(cmd);
					break;
				case "NEW_TOP":
					qstart_time = System.nanoTime();
					res=timed_top_consumer(Integer.parseInt(cmd[1]));
					qend_time = System.nanoTime();
					System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
					for (int i=0;i<res.size();i++) {
						System.out.println(res.get(i)+ ""+ res.get(i).consumed());
					}
					break;
				case "NEW_FLUSH":
					qstart_time = System.nanoTime();
					timed_flush( Integer.parseInt(cmd[1]));
					qend_time = System.nanoTime();
					System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
					break;
				default:
					System.err.println("Unknown command: " + cmd[0]);
				}

			}


			run_to_completion();
			print_stats();

		} catch (FileNotFoundException e) {
			System.err.println("Input file Not found. " + commandFile.getAbsolutePath());
		} catch (NullPointerException ne) {
			ne.printStackTrace();

		}
	}

	@Override
	public ArrayList<JobReport_> timed_report(String[] cmd) {
		long qstart_time, qend_time;
		ArrayList<JobReport_> res = null;
		switch (cmd[0]) {
		case "NEW_PROJECT":
			System.out.println("project query");
			qstart_time = System.nanoTime();
			res = handle_new_project(cmd);
			qend_time = System.nanoTime();
			System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
			for (int i=0;i<res.size();i++) {
				System.out.println(res.get(i));
			}
			break;
		case "NEW_USER":
			System.out.println("user query");

			qstart_time = System.nanoTime();
			res = handle_new_user(cmd);
			qend_time = System.nanoTime();
			System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
			for (int i=0;i<res.size();i++) {
				System.out.println(res.get(i));
			}
			break;
		case "NEW_PROJECTUSER":
			System.out.println("projectuser query");

			qstart_time = System.nanoTime();
			res = handle_new_projectuser(cmd);
			qend_time = System.nanoTime();
			System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
			System.out.println(res.size());
			for (int i=0;i<res.size();i++) {
				System.out.println(res.get(i));
			}
			break;
		case "NEW_PRIORITY":
			System.out.println("priority query");

			qstart_time = System.nanoTime();
			res = handle_new_priority(cmd[1]);
			qend_time = System.nanoTime();
			System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
			System.out.println(res.size());
			for (int i=0;i<res.size();i++) {
				System.out.println(res.get(i));

			}
			break;
		}

		return res;
	}
	Trie<Project> tr = new Trie<Project>();
	Trie<User> tr2 = new Trie<User>();
	MaxHeap<Job> sel = new MaxHeap<Job>();
	MaxHeap<Job> sel3 = new MaxHeap<Job>();
	static int g_t=0;
	RBTree<String,Job> rb = new RBTree<String,Job>();
	Trie<Job> j1 = new Trie<Job>();
	int jobcount=0;
	LinkedList<Job> link = new LinkedList<Job>();
	ArrayList<JobReport_> arjo = new ArrayList<JobReport_>();
	ArrayList<UserReport_> sort = new ArrayList<UserReport_>();

	@Override
	public ArrayList<UserReport_> timed_top_consumer(int top) {
		ArrayList<UserReport_> ar_so = new ArrayList<UserReport_>();
		int n = sort.size();
		int i;
		for(i=n-1; i>=0 ; i--) {
			int k =i+1;
			User piv = (User) sort.get(i);
			while ((k<n) &&((User)sort.get(k)).compareTo(piv)>0) {
				sort.set(k-1,sort.get(k));

				k++;
			}
			sort.set(k-1,piv);
		}
		if (top>sort.size()) {
			return sort;
		}
		else {
			int i1;
			for( i1=top;i1>=0;i1--) {
				ar_so.add((User) sort.get(i1));
			}
			return ar_so;
		}
	}


	@Override
	public void timed_flush(int waittime) {
		//    	System.out.println(sel.currentSize);
		MaxHeap<Job> mx1 = new MaxHeap<Job>();
		MaxHeap<Job> mx2 = new MaxHeap<Job>();
		int i ;
		for(i=1;i<=sel.size();i++) {
			Job pr_to_jo = ((Job)sel.queue.get(i));
			pr_to_jo.Waittime = g_t - pr_to_jo.arr_time ;
			if( pr_to_jo.Waittime>=waittime && pr_to_jo.obj.B>=pr_to_jo.t) {
				pr_to_jo.prior=9999;
				mx1.insert(pr_to_jo);
			}
			else {
				mx2.insert(pr_to_jo);
			}

		}
		//    	System.out.println(mx1.currentSize);
		//    	System.out.println(mx2.currentSize);
		//    	System.out.println(sel.currentSize);

		while(true) {

			Job sel1 ;
			sel1= mx1.extractMax();
			if (sel1==null) {
				break;
			}
			User u=sel1.obj1;
			//			System.out.println("Executing: "+sel1.name + " from: " +sel1.obj.name);
			if((sel1.obj.B)>=sel1.t) {

				sel1.prior = sel1.obj.priority;
				sel1.status="COMPLETED";
				g_t=g_t+sel1.t;
				sel1.end_time = g_t;
				link.add(sel1);
				sel1.obj.B = sel1.obj.B-sel1.t;
				u.consum = u.consum + sel1.t;
				u.last=g_t;


				//System.out.println("Project: "+ sel1.obj.name +" budget remaining: "+ sel1.obj.B);

				System.out.println(sel1.toString());


			}
			else {
				sel1.prior = sel1.obj.priority;
				mx2.insert(sel1);
			}
		}
		sel = mx2;
		//	System.out.println(sel.currentSize);

	}


	private ArrayList<JobReport_> handle_new_priority(String s) {
		ArrayList<JobReport_> ar_pr = new ArrayList<JobReport_>();
		int sint = Integer.parseInt(s);
		int i; 
		for(i=1;i<=sel3.size();i++) {
			if(sint<=((Job)sel3.queue.get(i)).prior && ((Job)sel3.queue.get(i)).status== "NOT FINISHED") {
				ar_pr.add((Job)sel3.queue.get(i));
			}
		}
		return ar_pr;
	}

	private ArrayList<JobReport_> handle_new_projectuser(String[] cmd) {
		TrieNode<User> se_us = tr2.search(cmd[2]);
		TrieNode<Project> se_pr = tr.search(cmd[1]);
		User us = se_us.getValue();
		ArrayList<Job> li4 =  us.arrli1;
		ArrayList<JobReport_> li5 = new ArrayList<JobReport_>();
		ArrayList<JobReport_> li6 = new ArrayList<JobReport_>();
		int str2 = Integer.parseInt(cmd[3]);
		int str3= Integer.parseInt(cmd[4]);
		int i;
		for(i=0; i<li4.size(); i++) {
			int k = li4.get(i).arr_time;
			if (k<=str3 && k>=str2) {
				li5.add(li4.get(i));
			}
		}
		int j;
		for (j=0; j<li5.size();j++) {
			if(li5.get(j).project_name().equals(cmd[1])) {
				li6.add(li5.get(j));
			}
		}
		return li6;
	}

	private ArrayList<JobReport_> handle_new_user(String[] cmd) {
		TrieNode<User> se_us = tr2.search(cmd[1]);
		if(se_us==null) {
			return new ArrayList<JobReport_>();
		}
		User us = se_us.getValue();
		ArrayList<Job> li2 =  us.arrli1;
		ArrayList<JobReport_> li3 = new ArrayList<JobReport_>();
		int str2 = Integer.parseInt(cmd[2]);
		int str3= Integer.parseInt(cmd[3]);
		int i;

		for(i=0; i<li2.size(); i++) {
			int k = li2.get(i).arr_time;
			if (k<=str3 && k>=str2) {
				li3.add(li2.get(i));
			}
		}
		return li3;
	}

	private ArrayList<JobReport_> handle_new_project(String[] cmd) {
		TrieNode<Project> se_pr = tr.search(cmd[1]);
		if(se_pr==null) {
			return new ArrayList<JobReport_>();
		}
		Project pro = se_pr.getValue();
		ArrayList<Job> li = pro.arrli;
		ArrayList<JobReport_> li1 = new ArrayList<JobReport_>();
		int str1 = Integer.parseInt(cmd[2]);
		int str = Integer.parseInt(cmd[3]);

		int i;
		for(i=0; i<li.size() ;i++ ) {
			int k = li.get(i).arr_time;
			if(k<=str && k>=str1) {
				li1.add(li.get(i));
			}
		}
		return li1 ;
	}


	public void schedule() {
		execute_a_job();
	}

	public void run_to_completion() {
		int B1;
		while(!sel.isEmpty()) {
			System.out.println("Running code");
			schedule();
			System.out.println("System execution completed");
		}
	}

	public void print_stats() {

		System.out.println("--------------STATS---------------");
		System.out.println("Total jobs done: "+ link.size());
		while(!link.isEmpty()) {
			Job sel2 ;
			sel2= link.remove();
			System.out.println(sel2.toString());
		}
		System.out.println("------------------------");
		System.out.println("Unfinished jobs: ");
		int yu=0;
		while(true) {
			Job sel4 ;
			sel4= sel3.extractMax();
			if(sel4==null) {
				break;
			}
			else {
				if(sel4.status.equals("NOT FINISHED")){
					System.out.println("Job{user=" +"'"+sel4.obj1.username+"'" +", project="+"'"+sel4.obj.name+"'"+", jobstatus=" +"REQUESTED"+ ", execution_time="+ sel4.t+", end_time="+"null"+ ", priority="+ sel4.prior+", name="+"'"+sel4.name+"'"+"}" );
					yu++;
				}
			}

		}
		System.out.println("Total unfinished jobs: "+yu);
		System.out.println("--------------STATS DONE---------------");

	}

	public void handle_add(String[] cmd) {
		System.out.println("ADDING Budget");
		TrieNode<Project> se = tr.search(cmd[1]);
		int s = Integer.parseInt(cmd[2]);
		if(se==null) {
			return;
		}
		se.getValue().B=se.getValue().B+ s;
		RedBlackNode<String, Job> rbnode = rb.search(cmd[1]);
		if(rbnode!=null && rbnode.getValues()!=null) {
			List<Job> li = rbnode.getValues();
			for(Job re:li) {
				sel.insert(re);
			}
			rbnode.values=new ArrayList<Job>();
		}
	}

	public void handle_empty_line() {

		System.out.println("Running code");
		schedule();

		System.out.println("Execution cycle completed");
	}


	public void handle_query(String key) {
		System.out.println("Querying");
		TrieNode<Job> def = j1.search(key);
		//System.out.println(key":"+" NOT FINISHED");

		if(def==null) {
			System.out.println(key+": "+"NO SUCH JOB");
		}
		else {
			System.out.println(key+": "+def.getValue().status);
		}
	}

	public void handle_user(String name) {
		User us = new User();
		us.username=name;
		tr2.insert(name, us);
		sort.add(us);
		System.out.println("Creating user");

	}

	public void handle_job(String[] cmd) {

		TrieNode<Project> rt = tr.search(cmd[2]);
		TrieNode<User> rt1 = tr2.search(cmd[3]);
		System.out.println("Creating job");


		if(rt==null) {

			System.out.println("No such project exists. "+cmd[2] );

			return;
		}
		if(rt1==null) {
			System.out.println("No such user exists: "+cmd[3] );
			return;
		}
		Job j = new Job(null, null);
		j.name=cmd[1];
		j.obj1= rt1.getValue();
		j.obj = rt.getValue();
		j.prior = j.obj.priority;
		j.t =Integer.parseInt(cmd[4]);
		boolean ate = tr2.insert(cmd[2],cmd[3]);

		String status = "NOT FINISHED";
		sel.insert(j);
		j.arr_time=g_t;
		sel3.insert(j);
		jobcount++;
		Project project = rt.getValue();
		project.arrli.add(j);
		User project1 = rt1.getValue();
		project1.arrli1.add(j);

		j1.insert(cmd[1], j);
	}

	public void handle_project(String[] cmd) {
		Project pr = new Project();
		pr.name = cmd[1];
		pr.priority= Integer.parseInt(cmd[2]);
		pr.B = Integer.parseInt(cmd[3]);
		tr.insert(cmd[1], pr);
		System.out.println("Creating project");
	}

	@Override
	public void timed_handle_user(String name) {
		User us = new User();
		us.username=name;
		tr2.insert(name, us);
		sort.add(us);

	}

	@Override
	public void timed_handle_job(String[] cmd) {

		TrieNode<Project> rt = tr.search(cmd[2]);
		TrieNode<User> rt1 = tr2.search(cmd[3]);


		if(rt==null) {


			return;
		}
		if(rt1==null) {
			return;
		}
		Job j = new Job(null, null);
		j.name=cmd[1];
		j.obj1= rt1.getValue();
		j.obj = rt.getValue();
		j.prior = j.obj.priority;
		j.t =Integer.parseInt(cmd[4]);
		boolean ate = tr2.insert(cmd[2],cmd[3]);

		String status = "NOT FINISHED";
		sel.insert(j);
		j.arr_time=g_t;
		sel3.insert(j);
		jobcount++;
		Project project = rt.getValue();
		project.arrli.add(j);
		User project1 = rt1.getValue();
		project1.arrli1.add(j);

		j1.insert(cmd[1], j);
	}

	@Override
	public void timed_handle_project(String[] cmd) {
		Project pr = new Project();
		pr.name = cmd[1];
		pr.priority= Integer.parseInt(cmd[2]);
		pr.B = Integer.parseInt(cmd[3]);
		tr.insert(cmd[1], pr);
	}

	@Override
	public void timed_run_to_completion() {
		int B1;
		while(!sel.isEmpty()) {
			execute_a_job2();
		}
	}

	public void execute_a_job() {

		System.out.println("	Remaining jobs: " + sel.currentSize);
		while(true) {

			Job sel1 ;
			sel1= sel.extractMax();

			if (sel1==null) {
				break;
			}
			User u=sel1.obj1;

			System.out.println("	Executing: "+sel1.name + " from: " +sel1.obj.name);
			if((sel1.obj.B)>=sel1.t) {
				sel1.status="COMPLETED";
				g_t=g_t+sel1.t;
				sel1.end_time = g_t;
				link.add(sel1);

				//sel1.status="COMPLETED";
				sel1.obj.B = sel1.obj.B-sel1.t;
				u.consum = u.consum + sel1.t;
				u.last=g_t;

				System.out.println("	Project: "+ sel1.obj.name +" budget remaining: "+ sel1.obj.B);

				break;

			}

			if(sel1.obj.B<sel1.t) {
				rb.insert(sel1.obj.name, sel1);
				System.out.println("	Un-sufficient budget.");

			}
		}
	}
	
	public void execute_a_job2() {

		while(true) {

			Job sel1 ;
			sel1= sel.extractMax();

			if (sel1==null) {
				break;
			}
			User u=sel1.obj1;

			if((sel1.obj.B)>=sel1.t) {
				sel1.status="COMPLETED";
				g_t=g_t+sel1.t;
				sel1.end_time = g_t;
				link.add(sel1);

				//sel1.status="COMPLETED";
				sel1.obj.B = sel1.obj.B-sel1.t;
				u.consum = u.consum + sel1.t;
				u.last=g_t;
				break;

			}

			if(sel1.obj.B<sel1.t) {
				rb.insert(sel1.obj.name, sel1);

			}
		}
	}

}
