package Trie;



import Util.NodeInterface;


public class TrieNode<T> implements NodeInterface<T> {
	T value;
	
	 int size= 53;
	public TrieNode[] children = new TrieNode[size];
	public TrieNode<T> parent;
	 boolean islast;
	
	
	public TrieNode(T value) {
		this.value=value;
		this.parent=parent;

		for (int i = 0; i < size; i++){
			children[i]=null;
		}
	}

    public T getValue() {
        return value;
        
    }

}	