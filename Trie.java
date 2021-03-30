package Trie;

public class Trie<T> implements TrieInterface {
	
	TrieNode<T> root;
	
	public Trie(){
		this.root=new TrieNode<T>(null);
	}


    public void printTrie(TrieNode trieNode) {
    	if(trieNode.islast) {
    		 System.out.println(trieNode.getValue().toString());
    	}
    	int i=0;
    	for(i=0;i<53;i++) {
    		if(trieNode.children[i]!=null) {
    			printTrie(trieNode.children[i]);
    		} 
    	}
    }

    @Override
    public boolean delete(String word) {
    	
    	if(search(word)==null) {
    		
    		return false;
    	}
    	TrieNode<T> tempo = search(word);
    	
    	tempo.islast=false;
    	tempo.value=null;
    	
    	int i =0;
    	boolean t=false;
    	
    	
    	int index=-1;
    	
    	while(tempo!=root) {
    		int in=0;
    		for(i=0;i<53;i++) {
        		
        		if(tempo.children[i]!=null) {
        			in++;
        		}
        	}
        	if(in >0) {
        		t=false;
        	}
        	else {
        		t=true;
        	}
        	for(i=0;i<53;i++) {
        		
        		if(tempo.parent.children[i]==tempo) {
        			index = i;
        			
        		}
        	}
        	tempo = tempo.parent;
    		if(t) {
    			tempo.children[index]=null;
    			in--;
    		}
    		else {
    			break;
    		}
    		
    	}
    	
		return true;

		
    	
    	
    }

    @Override
    public TrieNode<T> search(String word) {

		TrieNode<T> curr = root;
		int ind;

		for (int i = 0; i < word.length(); i++)
		{
			char ch = word.charAt(i);
    		if(ch>='A'&&ch<='Z') {
				ind=word.charAt(i) - 'A';
			}
			else if(ch>='a'&&ch<='z') {
				ind=word.charAt(i) - 'a'+26;
			}
			else {
				ind = 52;
			}
			curr = curr.children[ind];

			// if string is invalid (reached end of path in Trie)
			if (curr == null)
				return null;
		}

		// return true if current node is a leaf node and we have reached
		// the end of the string
		if(curr.islast) {
			return curr;
		}
		return null;
	}
    

    @Override
    public TrieNode<T> startsWith(String prefix) {
    	TrieNode<T> curr = root;
		int ind;

		for (int i = 0; i < prefix.length(); i++)
		{
			char ch = prefix.charAt(i);
    		if(ch>='A'&&ch<='Z') {
				ind=prefix.charAt(i) - 'A';
			}
			else if(ch>='a'&&ch<='z') {
				ind=prefix.charAt(i) - 'a'+26;
			}
			else {
				ind = 52;
			}
			curr = curr.children[ind];

			// if string is invalid (reached end of path in Trie)
			if (curr == null)
				return null;
		}

		// return true if current node is a leaf node and we have reached
		// the end of the string
		
		return curr;
    }

    @Override
    public boolean insert(String word, Object value) {
    	TrieNode<T> curr= root;
    	int lev = 0;
    	int ind;
    	boolean flag =false;
    	for (int i = 0; i < word.length(); i++)
		{
    		
    		char ch = word.charAt(i);
    		if(ch>='A'&&ch<='Z') {
				ind=word.charAt(i) - 'A';
			}
			else if(ch>='a'&&ch<='z') {
				ind=word.charAt(i) - 'a'+26;
				
			}
			else {
				ind = 52;
			}

			if (curr.children[ind] == null) {
				curr.children[ind]= new TrieNode(value);
				curr.children[ind].parent=curr;
				flag = true;
			}
			curr=curr.children[ind];
			
		}
    	curr.islast=true;
        return flag;
    }

    @Override
    public void printLevel(int level) {
    	int[] count = new int[52];
    	printlevel(root, level+1,0,count);
    	int flag= 1;
    	System.out.print("Level " + level + ": ");
    	for (int i = 0; i < 26; i++) {
    		for(int j=0;j<count[i];j++) {
    			if(flag==1) {
			System.out.print((char)('A'+i));
			flag=0;
    			}
    			else {
    				System.out.print(","+(char)('A'+i));
    				
    			}
    		}
		}
    	
    	for (int i = 26; i < 52; i++) {
    		for(int j=0;j<count[i];j++) {
    			if(flag==1) {
			System.out.print((char)('a'+i-26));
			flag=0;
    			}
    			else {
    				System.out.print(","+(char)('a'+i-26));

    			}
    		}
		}
    	System.out.println();
    	
    }

    private void printlevel(TrieNode<T> curr, int level,int index,int[] count) {
    	if (curr==null)
    		return;
    	if (level==1) {
    		if(index!=52)
    		 count[index]=count[index]+1;
    	}
    	else {
    		int i;
    		String s2="";
    		for(i=0;i<53;i++) {
    			if (curr.children[i]!=null) {
    				printlevel(curr.children[i], level-1,i,count);
    			}
    		}
       	}
    	
    	
    	
    }
    public String condition(int level) {
    	int[] count = new int[52];
    	String s="";
    	printlevel(root, level+1,0,count);
    	int flag= 1;
    	
    	for (int i = 0; i < 26; i++) {
    		for(int j=0;j<count[i];j++) {
    			if(flag==1) {
			s=s+(char)('A'+i);
			flag=0;
    			}
    			else {
    				s=s+","+(char)('A'+i);
    			}
    		}
		}
    	int flag1=1;
    	for (int i = 26; i < 52; i++) {
    		for(int j=0;j<count[i];j++) {
    			if(flag1==1) {
			s=s+(char)('a'+i-26);
			flag1=0;
    			}
    			else {
    				s=s+","+(char)('a'+i-26);
    			}
    		}
		}
    	return s;
    	
    }
    
    
    public void print() {
    	System.out.println("-------------");
    	System.out.println("Printing Trie");
    	int lev=1;
    	while(condition(lev)!="") {
//    		System.out.print("Level " + lev + ": ");
    		printLevel(lev);
    		lev++;
    	}
    	System.out.println("Level " + lev + ": ");
		System.out.println("-------------");

    }
	
}
