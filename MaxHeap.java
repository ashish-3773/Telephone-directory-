package PriorityQueue;

import java.util.ArrayList;

public class MaxHeap<T extends Comparable<T>> implements PriorityQueueInterface<T> {
	public ArrayList<T> queue;
	public int capacity = 10000, currentSize;
	int d=2;
	public MaxHeap() {    

		queue = new ArrayList<T>();
		queue.add(null);
		currentSize=0;
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return currentSize >= capacity;
	}

	public int upper(int a) {
		return (a)/2;
	}

	public boolean isEmpty() {
		return currentSize<=0;

	}


	@Override
	public void insert(T element) {
		if (isFull()) {
			return;
		}
	 	
		queue.add(element);
		currentSize++;
		int current = currentSize;
		while(current>1&&queue.get(current).compareTo(queue.get(upper(current)))>0) {
			T tempo = queue.get(current);
			queue.set(current, queue.get(upper(current)));
			//queue[current]=queue[upper(current)];
			queue.set(upper(current), tempo);
			current = upper(current);
			
		}
		//print();
	}

	public T extractMax() {
		if(currentSize==0) {
			return null;
		}
		T extr =queue.get(1);
		queue.set(1,queue.get(currentSize--));
		queue.remove(currentSize+1);
		heapy(1);
		return (T) extr;

	}

	public void heapy(int p) {
		int leftchild= 2*p;
		int rightchild= 2*p+1;
		if(leftchild>currentSize) {
			return;
		}
		if(rightchild>currentSize) {
			if((queue.get(p).compareTo(queue.get(leftchild))<0)) {
					T tempo = queue.get(p);
					queue.set(p,queue.get(leftchild));
					queue.set(leftchild,tempo);
					heapy(leftchild);
			}
			return;
		}
		if((queue.get(p).compareTo(queue.get(leftchild))<0) || (queue.get(p).compareTo(queue.get(rightchild))<0 )) {
			if(queue.get(leftchild).compareTo(queue.get(rightchild))>0) {
				T tempo = queue.get(p);
				queue.set(p,queue.get(leftchild));
				queue.set(leftchild,tempo);
				heapy(leftchild);

			}
			else {
				T tempo = queue.get(p);
				queue.set(p,queue.get(rightchild));
				queue.set(rightchild,tempo);
				heapy(rightchild);
			}
		}
	}

	public void moveup(int i) {
		
	}
//	public void print() {
//		for (int i = 1; i < queue.length; i++) {
//			Comparable comparable = queue[i];
//			if(queue[i]!=null) {
//				System.out.println(i+" "+queue[i]);
//			}
//			else {
//				break;
//			}
//			
//		}
//	}
//
//

}