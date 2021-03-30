package RedBlack;

import Util.RBNodeInterface;

import java.util.ArrayList;
import java.util.List;

public class RedBlackNode<T extends Comparable, E> implements RBNodeInterface<E> {
	public 	T key;
	public ArrayList<E> values;
	public RedBlackNode<T,E> parent;
	public RedBlackNode<T,E> left;
	public RedBlackNode<T,E> right;
	public RedBlackNode<T,E> rightChild;
	public RedBlackNode<T,E> leftChild;
	public int  colour;
	
	public RedBlackNode(T k,E v) {
		key = k;
		values = new ArrayList<E>();
		values.add(v);
		parent=null;
		left=null;
		right=null ;
		rightChild=null;
		leftChild=null;
		colour=1;

		
	}

    @Override
    public E getValue() {
        return values.get(0);
    }

    @Override
    public List<E> getValues() {
        return values;
    }
}
