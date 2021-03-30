package RedBlack;


public class RBTree<T extends Comparable, E> implements RBTreeInterface<T, E>  {
	 RedBlackNode<T, E> root;
		
	 public RBTree(){
     root = null;
     }
	public void leftRotate(RedBlackNode<T,E> rt ) {
		RedBlackNode<T,E> rt1 = rt.rightChild;
		rt.rightChild = rt1.leftChild;
		if(rt1.leftChild != null){
			rt1.leftChild.parent = rt;
		}
			rt1.parent = rt.parent;
			if(rt.parent == null)
				this.root = rt1;
			    else if(rt == rt.parent.leftChild){
	       rt.parent.leftChild = rt1;
	   }
	   else{
	       rt.parent.rightChild = rt1;
	   }
	   rt1.leftChild = rt;
	   rt.parent = rt1;
	  }

	  public void rightRotate(RedBlackNode<T,E> rt){
		  RedBlackNode<T,E> rt1 = rt.leftChild;
		 // System.out.println();
	     rt.leftChild = rt1.rightChild;
	     if(rt1.rightChild != null){
	         rt1.rightChild.parent = rt;
	     }
	     rt1.parent = rt.parent;
	     if(rt.parent == null){
	         this.root = rt1;
	     }
	     else if(rt == rt.parent.rightChild){
	         rt.parent.rightChild = rt1;
	     }
	     else{
	         rt.parent.leftChild = rt1;
	     }
	     rt1.rightChild = rt;
	     rt.parent = rt1;
	 }
	 
	public void arran(RedBlackNode<T, E> curr) {
		RedBlackNode<T, E> parent = curr.parent;
		if(curr==root) {
			curr.colour=0;
			return;
		}
		//System.out.println(curr.parent.parent);
		if(curr.parent.colour!=0 && curr!=root) {
			RedBlackNode<T, E> uncle;
			if (curr.parent.parent.left==curr.parent) {
				 uncle= curr.parent.parent.right; 
			}
			else {
				uncle= curr.parent.parent.left;
			}
			if(uncle!=null &&uncle.colour==1) {
				parent.colour=0;
				uncle.colour=0;
				
				arran(curr.parent.parent);
				
			
			}	
			if(uncle==null||uncle.colour==0) {
				RedBlackNode<T, E> p = curr.parent;
				RedBlackNode<T, E> g = curr.parent.parent;
				if(p==g.leftChild && curr==p.leftChild) {
					rightRotate(g);
					int tempo = p.colour;
					p.colour=g.colour;
					g.colour=tempo;
				}
				if(p==g.leftChild && curr==p.rightChild) {
					leftRotate(p);
					rightRotate(g);
					int tempo = p.colour;
					p.colour=g.colour;
					g.colour=tempo;
				}
				if(p==g.rightChild && curr==p.leftChild) {
					leftRotate(g);
					int tempo = p.colour;
					p.colour=g.colour;
					g.colour=tempo;
				}
				if(p==g.rightChild && curr==p.leftChild) {
					rightRotate(p);
					leftRotate(g);
					int tempo = p.colour;
					p.colour=g.colour;
					g.colour=tempo;
				}
			}
		}
	} 
	
    @Override
    public void insert(T key, E value) {
		RedBlackNode<T, E> node = search(key);
		if(node!=null) {
			node.values.add(value);
			return;
		}

    	
		RedBlackNode<T, E> curr = root;
		int i = 1;

		RedBlackNode<T, E> uncle= null;
  
  
		RedBlackNode<T, E> parent = null;

		if (root == null) {
			root= new RedBlackNode<T, E>(key,value);
			root.colour=0;
			return;
		}
		else {
		while (curr != null)
		{
			parent = curr;
			i++;

			if (key.compareTo(curr.key)<0) {
				curr = curr.left;
			}
			else {
				curr = curr.right;
			}
		}
		}
		if (key .compareTo(parent.key)<0) {
			curr = parent.left = new RedBlackNode<T, E>(key,value);
			//i++;
		}
		else {
			curr = parent.right = new RedBlackNode<T, E>(key,value);
		//	i++;
		}
		curr.parent = parent;
		
		arran(curr);
		
    }

    @Override
    public RedBlackNode<T, E> search(T key) {
    	RedBlackNode<T, E> curr = root;
		while (curr != null)
		{
			//System.out.println(curr.key+" "+curr.key.compareTo(key));
			if(curr.key.compareTo(key)==0) {
				return curr;
			}
			
			
			if (key.compareTo(curr.key)<0) {
				curr = curr.left;
			}
			else {
				curr = curr.right;
			}
		}
		
		return null;
    }
}
//public void rightRotate(RedBlackNode<T,E> rt){
//	  RedBlackNode<T,E> rt1 = rt.leftChild;
//	 // System.out.println();
//   rt.leftChild = rt1.rightChild;
//   if(rt1.rightChild != null){
//       rt1.rightChild.parent = rt;
//   }
//   rt1.parent = rt.parent;
//   if(rt.parent == null){
//       this.root = rt1;
//   }
//   else if(rt == rt.parent.rightChild){
//       rt.parent.rightChild = rt1;
//   }
//   else{
//       rt.parent.leftChild = rt1;
//   }
//   rt1.rightChild = rt;
//   rt.parent = rt1;
//}
