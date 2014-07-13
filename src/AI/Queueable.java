package AI;

import java.util.ArrayDeque;

import Hotel.MapPoint;

public interface Queueable<T> {
	
	/**
	 * Add the given element to the end of the queue
	 * @param element to be added to the queue.
	 */
	public void enqueue(T element);
	
	/**
	 * Position of the start of the queue
	 * @return Position that guests walk to to be at the start of the queue
	 */
	public MapPoint queuePosition();
	
	/**
	 * Polls first element from the queue
	 * @return Removes from the queue and returns
	 */
	public T dequeue();
	
	/**
	 * Peeks at the element at the end of the queue
	 * @return
	 */
	public T getLastInQueue();
	
	/**
	 * Returns the queue
	 * @return ArrayDequeue
	 */
	public ArrayDeque<T> getQueue();
}
