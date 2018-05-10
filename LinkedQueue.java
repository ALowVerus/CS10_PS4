import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Linked list queue, taken from online:
 * http://cs-fundamentals.com/data-structures/implement-queue-using-linked-list-in-java.php
 */
class LinkedQueue<V> implements Queue <V> {
	private Node front, rear; //begin and end nodes
	private int size; // number of items
 
	//nested class to define node
	private class Node
	{ 
	V item;
	Node next;
	}
 
	//Zero argument constructor
	public LinkedQueue() {
		front = null;
		rear = null;
		size = 0;
	}
 
	public boolean isEmpty() {
		return (size == 0);
	}
 
	//Remove item from the beginning of the list.
	public V dequeue() {
		V item = front.item;
		front = front.next;
		if (isEmpty()) {
			rear = null;
		}
		size--;
		return item;
	}
 
	//Add item to the end of the list.
	public void enqueue(V item) {
		Node oldRear = rear;
		rear = new Node();
		rear.item = item;
		rear.next = null;
		if (isEmpty()) {
			front = rear;
		}
		else {
			oldRear.next = rear;
		}
		size++;
	}
 
	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<V> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean add(V e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offer(V e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V remove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V peek() {
		// TODO Auto-generated method stub
		return null;
	}
}