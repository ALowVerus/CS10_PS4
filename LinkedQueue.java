import java.util.Queue;

/**
 * Linked list queue, taken from online:
 * http://cs-fundamentals.com/data-structures/implement-queue-using-linked-list-in-java.php
 */
class LinkedQueue <V> implements Queue <V> {
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
}