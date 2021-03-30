package Trie;

public class Person {
	String name;
	String phone;
    public Person(String name, String phone_number) {
    	this.name=name;
    	phone=phone_number;
    }

    public String getName() {
        return name;
    }
    public String toString() {
		return "[Name: " +name+", Phone=" +phone+"]";
		
    	
    }
}
