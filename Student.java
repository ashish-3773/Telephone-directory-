package PriorityQueue;

public class Student implements Comparable<Student> {
    private String name;
    private Integer marks;
    public int count;
    static private int c=0;
    public Student(String trim, int parseInt) {
    	name=trim ;
    	marks=parseInt;
    	count=c++;
    }


    @Override
    public int compareTo(Student student) {
    	if (marks<student.marks) {
    		return -1;
    	}
    	else if (marks>student.marks){
    		return 1;
    	}
    	else {
    		if(count==student.count) {
    			return 0;
    		}
    		else if(count<student.count) {
    			return 1;
    		}
    		else {
    			return -1;
    		}
    	}
       
    }

    public String getName() {
        return name;
    }
    public String toString() {
    	return "Student{name='"+name+"', marks="+marks+"}";
    }
}
