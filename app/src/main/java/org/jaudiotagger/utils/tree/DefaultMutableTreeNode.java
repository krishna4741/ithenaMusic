

package org.jaudiotagger.utils.tree;
   // ISSUE: this class depends on nothing in AWT -- move to java.util?

import java.io.*;
import java.util.*;



public class DefaultMutableTreeNode extends Object implements Cloneable,
       MutableTreeNode, Serializable
{


    static public final Enumeration<TreeNode> EMPTY_ENUMERATION
	= new Enumeration<TreeNode>() {
	    public boolean hasMoreElements() { return false; }
	    public TreeNode nextElement() {
		throw new NoSuchElementException("No more elements");
	    }
    };


    protected MutableTreeNode   parent;


    protected Vector children;


    transient protected Object	userObject;


    protected boolean		allowsChildren;



    public DefaultMutableTreeNode() {
	this(null);
    }


    public DefaultMutableTreeNode(Object userObject) {
	this(userObject, true);
    }


    public DefaultMutableTreeNode(Object userObject, boolean allowsChildren) {
	super();
	parent = null;
	this.allowsChildren = allowsChildren;
	this.userObject = userObject;
    }


    //
    //  Primitives
    //


    public void insert(MutableTreeNode newChild, int childIndex) {
	if (!allowsChildren) {
	    throw new IllegalStateException("node does not allow children");
	} else if (newChild == null) {
	    throw new IllegalArgumentException("new child is null");
	} else if (isNodeAncestor(newChild)) {
	    throw new IllegalArgumentException("new child is an ancestor");
	}

	    MutableTreeNode oldParent = (MutableTreeNode)newChild.getParent();

	    if (oldParent != null) {
		oldParent.remove(newChild);
	    }
	    newChild.setParent(this);
	    if (children == null) {
		children = new Vector();
	    }
	    children.insertElementAt(newChild, childIndex);
    }


    public void remove(int childIndex) {
	MutableTreeNode child = (MutableTreeNode)getChildAt(childIndex);
	children.removeElementAt(childIndex);
	child.setParent(null);
    }


    public void setParent(MutableTreeNode newParent) {
	parent = newParent;
    }


    public TreeNode getParent() {
	return parent;
    }


    public TreeNode getChildAt(int index) {
	if (children == null) {
	    throw new ArrayIndexOutOfBoundsException("node has no children");
	}
	return (TreeNode)children.elementAt(index);
    }


    public int getChildCount() {
	if (children == null) {
	    return 0;
	} else {
	    return children.size();
	}
    }


    public int getIndex(TreeNode aChild) {
	if (aChild == null) {
	    throw new IllegalArgumentException("argument is null");
	}

	if (!isNodeChild(aChild)) {
	    return -1;
	}
	return children.indexOf(aChild);	// linear search
    }


    public Enumeration children() {
	if (children == null) {
	    return EMPTY_ENUMERATION;
	} else {
	    return children.elements();
	}
    }


    public void setAllowsChildren(boolean allows) {
	if (allows != allowsChildren) {
	    allowsChildren = allows;
	    if (!allowsChildren) {
		removeAllChildren();
	    }
	}
    }


    public boolean getAllowsChildren() {
	return allowsChildren;
    }


    public void setUserObject(Object userObject) {
	this.userObject = userObject;
    }


    public Object getUserObject() {
	return userObject;
    }


    //
    //  Derived methods
    //


    public void removeFromParent() {
	MutableTreeNode parent = (MutableTreeNode)getParent();
	if (parent != null) {
	    parent.remove(this);
	}
    }


    public void remove(MutableTreeNode aChild) {
	if (aChild == null) {
	    throw new IllegalArgumentException("argument is null");
	}

	if (!isNodeChild(aChild)) {
	    throw new IllegalArgumentException("argument is not a child");
	}
	remove(getIndex(aChild));	// linear search
    }


    public void removeAllChildren() {
	for (int i = getChildCount()-1; i >= 0; i--) {
	    remove(i);
	}
    }


    public void add(MutableTreeNode newChild) {
	if(newChild != null && newChild.getParent() == this)
	    insert(newChild, getChildCount() - 1);
	else
	    insert(newChild, getChildCount());
    }



    //
    //  Tree Queries
    //


    public boolean isNodeAncestor(TreeNode anotherNode) {
	if (anotherNode == null) {
	    return false;
	}

	TreeNode ancestor = this;

	do {
	    if (ancestor == anotherNode) {
		return true;
	    }
	} while((ancestor = ancestor.getParent()) != null);

	return false;
    }


    public boolean isNodeDescendant(DefaultMutableTreeNode anotherNode) {
	if (anotherNode == null)
	    return false;

	return anotherNode.isNodeAncestor(this);
    }


    public TreeNode getSharedAncestor(DefaultMutableTreeNode aNode) {
	if (aNode == this) {
	    return this;
	} else if (aNode == null) {
	    return null;
	}

	int		level1, level2, diff;
	TreeNode	node1, node2;
	
	level1 = getLevel();
	level2 = aNode.getLevel();
	
	if (level2 > level1) {
	    diff = level2 - level1;
	    node1 = aNode;
	    node2 = this;
	} else {
	    diff = level1 - level2;
	    node1 = this;
	    node2 = aNode;
	}

	// Go up the tree until the nodes are at the same level
	while (diff > 0) {
	    node1 = node1.getParent();
	    diff--;
	}
	
	// Move up the tree until we find a common ancestor.  Since we know
	// that both nodes are at the same level, we won'timer cross paths
	// unknowingly (if there is a common ancestor, both nodes hit it in
	// the same iteration).
	
	do {
	    if (node1 == node2) {
		return node1;
	    }
	    node1 = node1.getParent();
	    node2 = node2.getParent();
	} while (node1 != null);// only need to check one -- they're at the
	// same level so if one is null, the other is
	
	if (node1 != null || node2 != null) {
	    throw new Error ("nodes should be null");
	}
	
	return null;
    }



    public boolean isNodeRelated(DefaultMutableTreeNode aNode) {
	return (aNode != null) && (getRoot() == aNode.getRoot());
    }



    public int getDepth() {
	Object	last = null;
	Enumeration	enum_ = breadthFirstEnumeration();
	
	while (enum_.hasMoreElements()) {
	    last = enum_.nextElement();
	}
	
	if (last == null) {
	    throw new Error ("nodes should be null");
	}
	
	return ((DefaultMutableTreeNode)last).getLevel() - getLevel();
    }




    public int getLevel() {
	TreeNode ancestor;
	int levels = 0;

	ancestor = this;
	while((ancestor = ancestor.getParent()) != null){
	    levels++;
	}

	return levels;
    }



    public TreeNode[] getPath() {
	return getPathToRoot(this, 0);
    }


    protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
	TreeNode[]              retNodes;


	if(aNode == null) {
	    if(depth == 0)
		return null;
	    else
		retNodes = new TreeNode[depth];
	}
	else {
	    depth++;
	    retNodes = getPathToRoot(aNode.getParent(), depth);
	    retNodes[retNodes.length - depth] = aNode;
	}
	return retNodes;
    }


    public Object[] getUserObjectPath() {
	TreeNode[]          realPath = getPath();
	Object[]            retPath = new Object[realPath.length];

	for(int counter = 0; counter < realPath.length; counter++)
	    retPath[counter] = ((DefaultMutableTreeNode)realPath[counter])
		               .getUserObject();
	return retPath;
    }


    public TreeNode getRoot() {
	TreeNode ancestor = this;
	TreeNode previous;

	do {
	    previous = ancestor;
	    ancestor = ancestor.getParent();
	} while (ancestor != null);

	return previous;
    }



    public boolean isRoot() {
	return getParent() == null;
    }



    public DefaultMutableTreeNode getNextNode() {
	if (getChildCount() == 0) {
	    // No children, so look for nextSibling
	    DefaultMutableTreeNode nextSibling = getNextSibling();

	    if (nextSibling == null) {
		DefaultMutableTreeNode aNode = (DefaultMutableTreeNode)getParent();

		do {
		    if (aNode == null) {
			return null;
		    }

		    nextSibling = aNode.getNextSibling();
		    if (nextSibling != null) {
			return nextSibling;
		    }

		    aNode = (DefaultMutableTreeNode)aNode.getParent();
		} while(true);
	    } else {
		return nextSibling;
	    }
	} else {
	    return (DefaultMutableTreeNode)getChildAt(0);
	}
    }



    public DefaultMutableTreeNode getPreviousNode() {
	DefaultMutableTreeNode previousSibling;
	DefaultMutableTreeNode myParent = (DefaultMutableTreeNode)getParent();

	if (myParent == null) {
	    return null;
	}

	previousSibling = getPreviousSibling();

	if (previousSibling != null) {
	    if (previousSibling.getChildCount() == 0)
		return previousSibling;
	    else
		return previousSibling.getLastLeaf();
	} else {
	    return myParent;
	}
    }


    public Enumeration preorderEnumeration() {
	return new PreorderEnumeration(this);
    }


    public Enumeration postorderEnumeration() {
	return new PostorderEnumeration(this);
    }


    public Enumeration breadthFirstEnumeration() {
	return new BreadthFirstEnumeration(this);
    }


    public Enumeration depthFirstEnumeration() {
	return postorderEnumeration();
    }


    public Enumeration pathFromAncestorEnumeration(TreeNode ancestor) {
	return new PathBetweenNodesEnumeration(ancestor, this);
    }


    //
    //  Child Queries
    //


    public boolean isNodeChild(TreeNode aNode) {
	boolean retval;

	if (aNode == null) {
	    retval = false;
	} else {
	    if (getChildCount() == 0) {
		retval = false;
	    } else {
		retval = (aNode.getParent() == this);
	    }
	}

	return retval;
    }



    public TreeNode getFirstChild() {
	if (getChildCount() == 0) {
	    throw new NoSuchElementException("node has no children");
	}
	return getChildAt(0);
    }



    public TreeNode getLastChild() {
	if (getChildCount() == 0) {
	    throw new NoSuchElementException("node has no children");
	}
	return getChildAt(getChildCount()-1);
    }



    public TreeNode getChildAfter(TreeNode aChild) {
	if (aChild == null) {
	    throw new IllegalArgumentException("argument is null");
	}

	int index = getIndex(aChild);		// linear search

	if (index == -1) {
	    throw new IllegalArgumentException("node is not a child");
	}

	if (index < getChildCount() - 1) {
	    return getChildAt(index + 1);
	} else {
	    return null;
	}
    }



    public TreeNode getChildBefore(TreeNode aChild) {
	if (aChild == null) {
	    throw new IllegalArgumentException("argument is null");
	}

	int index = getIndex(aChild);		// linear search

	if (index == -1) {
	    throw new IllegalArgumentException("argument is not a child");
	}

	if (index > 0) {
	    return getChildAt(index - 1);
	} else {
	    return null;
	}
    }


    //
    //  Sibling Queries
    //



    public boolean isNodeSibling(TreeNode anotherNode) {
	boolean retval;

	if (anotherNode == null) {
	    retval = false;
	} else if (anotherNode == this) {
	    retval = true;
	} else {
	    TreeNode  myParent = getParent();
	    retval = (myParent != null && myParent == anotherNode.getParent());

	    if (retval && !((DefaultMutableTreeNode)getParent())
		           .isNodeChild(anotherNode)) {
		throw new Error("sibling has different parent");
	    }
	}

	return retval;
    }



    public int getSiblingCount() {
	TreeNode myParent = getParent();

	if (myParent == null) {
	    return 1;
	} else {
	    return myParent.getChildCount();
	}
    }



    public DefaultMutableTreeNode getNextSibling() {
	DefaultMutableTreeNode retval;

	DefaultMutableTreeNode myParent = (DefaultMutableTreeNode)getParent();

	if (myParent == null) {
	    retval = null;
	} else {
	    retval = (DefaultMutableTreeNode)myParent.getChildAfter(this);	// linear search
	}

	if (retval != null && !isNodeSibling(retval)) {
	    throw new Error("child of parent is not a sibling");
	}

	return retval;
    }



    public DefaultMutableTreeNode getPreviousSibling() {
	DefaultMutableTreeNode retval;

	DefaultMutableTreeNode myParent = (DefaultMutableTreeNode)getParent();

	if (myParent == null) {
	    retval = null;
	} else {
	    retval = (DefaultMutableTreeNode)myParent.getChildBefore(this);	// linear search
	}

	if (retval != null && !isNodeSibling(retval)) {
	    throw new Error("child of parent is not a sibling");
	}

	return retval;
    }



    //
    //  Leaf Queries
    //


    public boolean isLeaf() {
	return (getChildCount() == 0);
    }



    public DefaultMutableTreeNode getFirstLeaf() {
	DefaultMutableTreeNode node = this;

	while (!node.isLeaf()) {
	    node = (DefaultMutableTreeNode)node.getFirstChild();
	}

	return node;
    }



    public DefaultMutableTreeNode getLastLeaf() {
	DefaultMutableTreeNode node = this;

	while (!node.isLeaf()) {
	    node = (DefaultMutableTreeNode)node.getLastChild();
	}

	return node;
    }



    public DefaultMutableTreeNode getNextLeaf() {
	DefaultMutableTreeNode nextSibling;
	DefaultMutableTreeNode myParent = (DefaultMutableTreeNode)getParent();

	if (myParent == null)
	    return null;

	nextSibling = getNextSibling();	// linear search

	if (nextSibling != null)
	    return nextSibling.getFirstLeaf();

	return myParent.getNextLeaf();	// tail recursion
    }



    public DefaultMutableTreeNode getPreviousLeaf() {
	DefaultMutableTreeNode previousSibling;
	DefaultMutableTreeNode myParent = (DefaultMutableTreeNode)getParent();

	if (myParent == null)
	    return null;

	previousSibling = getPreviousSibling();	// linear search

	if (previousSibling != null)
	    return previousSibling.getLastLeaf();

	return myParent.getPreviousLeaf();		// tail recursion
    }



    public int getLeafCount() {
	int count = 0;

	TreeNode node;
	Enumeration enum_ = breadthFirstEnumeration(); // order matters not

	while (enum_.hasMoreElements()) {
	    node = (TreeNode)enum_.nextElement();
	    if (node.isLeaf()) {
		count++;
	    }
	}

	if (count < 1) {
	    throw new Error("tree has zero leaves");
	}

	return count;
    }


    //
    //  Overrides
    //


    public String toString() {
	if (userObject == null) {
	    return null;
	} else {
	    return userObject.toString();
	}
    }


    public Object clone() {
	DefaultMutableTreeNode newNode = null;

	try {
	    newNode = (DefaultMutableTreeNode)super.clone();

	    // shallow copy -- the new node has no parent or children
	    newNode.children = null;
	    newNode.parent = null;

	} catch (CloneNotSupportedException e) {
	    // Won'timer happen because we implement Cloneable
	    throw new Error(e.toString());
	}

	return newNode;
    }


    // Serialization support.  
    private void writeObject(ObjectOutputStream s) throws IOException {
	Object[]             tValues;

	s.defaultWriteObject();
	// Save the userObject, if its Serializable.
	if(userObject != null && userObject instanceof Serializable) {
	    tValues = new Object[2];
	    tValues[0] = "userObject";
	    tValues[1] = userObject;
	}
	else
	    tValues = new Object[0];
	s.writeObject(tValues);
    }

    private void readObject(ObjectInputStream s) 
	throws IOException, ClassNotFoundException {
	Object[]      tValues;

	s.defaultReadObject();

	tValues = (Object[])s.readObject();

	if(tValues.length > 0 && tValues[0].equals("userObject"))
	    userObject = tValues[1];
    }

    final class PreorderEnumeration implements Enumeration<TreeNode> {
	protected Stack stack;

	public PreorderEnumeration(TreeNode rootNode) {
	    super();
	    Vector v = new Vector(1);
	    v.addElement(rootNode);	// PENDING: don'timer really need a vector
	    stack = new Stack();
	    stack.push(v.elements());
	}

	public boolean hasMoreElements() {
	    return (!stack.empty() &&
		    ((Enumeration)stack.peek()).hasMoreElements());
	}

	public TreeNode nextElement() {
	    Enumeration	enumer = (Enumeration)stack.peek();
	    TreeNode	node = (TreeNode)enumer.nextElement();
	    Enumeration	children = node.children();

	    if (!enumer.hasMoreElements()) {
		stack.pop();
	    }
	    if (children.hasMoreElements()) {
		stack.push(children);
	    }
	    return node;
	}

    }  // End of class PreorderEnumeration



    final class PostorderEnumeration implements Enumeration<TreeNode> {
	protected TreeNode root;
	protected Enumeration<TreeNode> children;
	protected Enumeration<TreeNode> subtree;

	public PostorderEnumeration(TreeNode rootNode) {
	    super();
	    root = rootNode;
	    children = root.children();
	    subtree = EMPTY_ENUMERATION;
	}

	public boolean hasMoreElements() {
	    return root != null;
	}

	public TreeNode nextElement() {
	    TreeNode retval;

	    if (subtree.hasMoreElements()) {
		retval = subtree.nextElement();
	    } else if (children.hasMoreElements()) {
		subtree = new PostorderEnumeration(
				(TreeNode)children.nextElement());
		retval = subtree.nextElement();
	    } else {
		retval = root;
		root = null;
	    }

	    return retval;
	}

    }  // End of class PostorderEnumeration



    final class BreadthFirstEnumeration implements Enumeration<TreeNode> {
	protected Queue	queue;

	public BreadthFirstEnumeration(TreeNode rootNode) {
	    super();
	    Vector v = new Vector(1);
	    v.addElement(rootNode);	// PENDING: don'timer really need a vector
	    queue = new Queue();
	    queue.enqueue(v.elements());
	}

	public boolean hasMoreElements() {
	    return (!queue.isEmpty() &&
		    ((Enumeration)queue.firstObject()).hasMoreElements());
	}

	public TreeNode nextElement() {
	    Enumeration	enumer = (Enumeration)queue.firstObject();
	    TreeNode	node = (TreeNode)enumer.nextElement();
	    Enumeration	children = node.children();

	    if (!enumer.hasMoreElements()) {
		queue.dequeue();
	    }
	    if (children.hasMoreElements()) {
		queue.enqueue(children);
	    }
	    return node;
	}


	// A simple queue with a linked list data structure.
	final class Queue {
	    QNode head;	// null if empty
	    QNode tail;

	    final class QNode {
		public Object	object;
		public QNode	next;	// null if end
		public QNode(Object object, QNode next) {
		    this.object = object;
		    this.next = next;
		}
	    }

	    public void enqueue(Object anObject) {
		if (head == null) {
		    head = tail = new QNode(anObject, null);
		} else {
		    tail.next = new QNode(anObject, null);
		    tail = tail.next;
		}
	    }

	    public Object dequeue() {
		if (head == null) {
		    throw new NoSuchElementException("No more elements");
		}

		Object retval = head.object;
		QNode oldHead = head;
		head = head.next;
		if (head == null) {
		    tail = null;
		} else {
		    oldHead.next = null;
		}
		return retval;
	    }

	    public Object firstObject() {
		if (head == null) {
		    throw new NoSuchElementException("No more elements");
		}

		return head.object;
	    }

	    public boolean isEmpty() {
		return head == null;
	    }

	} // End of class Queue

    }  // End of class BreadthFirstEnumeration



    final class PathBetweenNodesEnumeration implements Enumeration<TreeNode> {
	protected Stack<TreeNode> stack;

	public PathBetweenNodesEnumeration(TreeNode ancestor,
					   TreeNode descendant)
	{
	    super();

	    if (ancestor == null || descendant == null) {
		throw new IllegalArgumentException("argument is null");
	    }

	    TreeNode current;

	    stack = new Stack<TreeNode>();
	    stack.push(descendant);

	    current = descendant;
	    while (current != ancestor) {
		current = current.getParent();
		if (current == null && descendant != ancestor) {
		    throw new IllegalArgumentException("node " + ancestor +
				" is not an ancestor of " + descendant);
		}
		stack.push(current);
	    }
	}

	public boolean hasMoreElements() {
	    return stack.size() > 0;
	}

	public TreeNode nextElement() {
	    try {
		return stack.pop();
	    } catch (EmptyStackException e) {
		throw new NoSuchElementException("No more elements");
	    }
	}

    } // End of class PathBetweenNodesEnumeration



} // End of class DefaultMutableTreeNode
