package AI;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;

public class LineQueue<E> implements Iterable<E>{

	private ArrayDeque<LineQueuePosition<E>> queue = new ArrayDeque<LineQueuePosition<E>>();
	
	public LineQueue(){}
	
	public LineQueue(E[] e){
		if( e != null && e.length > 0 ){
			for(E element : e){
				add(element);
			}
		}
	}
	
	public boolean add(E e){
		LineQueuePosition<E> element = new LineQueuePosition<E>(queue.peekLast(),e,null);
		
		// Tell the next element's parent to point to us
		if( element.getParent() != null ){
			element.getParent().setChild(element);
		}
		
		return queue.add(element);
	}
	
	public void	addFirst(E e){
		LineQueuePosition<E> element = new LineQueuePosition<E>(null,e,queue.peekFirst());
		
		// Tell the next element's parent to point to us
		if( queue.peekFirst() != null ){
			queue.peekFirst().setParent(element);
		}
		
		queue.addFirst(element);
	}
	
	public void	addLast(E e){
		LineQueuePosition<E> element = new LineQueuePosition<E>(queue.peekLast(),e,null);
		
		// Tell the next element's parent to point to us
		if( element.getParent() != null ){
			element.getParent().setChild(element);
		}
		
		queue.addLast(element);
	}
	
	public void clear(){
		queue.clear();
	}
	
	public boolean contains(Object e){
		
		LineQueuePosition<E> node = queue.peekFirst();
		while( node != null ){
			if( node.equals(e) ){
				return true;
			}
			node = node.getChild();
		}
		
		
		return false;		
		// Does not work
		//return queue.contains(e);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public int size(){
		return queue.size();
	}
	
	public LineQueuePosition<E> peek(){
		return queue.peek();
	}
	
	public LineQueuePosition<E> peekFirst(){
		return queue.peekFirst();
	}
	
	public LineQueuePosition<E> peekLast(){
		return queue.peekLast();
	}
	
	public LineQueuePosition<E> pop(){
		LineQueuePosition<E> first = queue.pop();
		queue.peekFirst().setParent(null);
		
		return first;
	}
	
	/**
	 * Not implemented, can not remove from queue
	 * @param element
	 * @return
	 */
	public boolean remove(E element){
		return false;
		/*if( element == null ){
			return false;
		}
		
		boolean removed = queue.remove(element);
		if( removed ){
			// Set Parent to point to child
			//element.getParent().setChild(element.getChild());
			
			// Set child to point to Parent
			//element.getParent().setParent(element.getParent());
		}
		
		return removed;*/
	}

	@Override
	public Iterator<E> iterator() {
		return new LineQueueIterator(this);
	}
	
	public Iterable<E> descendingIterator() {
		return new LineQueueDescendingIterator(this);
	}
	
	private class LineQueueIterator implements Iterator<E>{
		
		private LineQueuePosition<E> node;
		
		public LineQueueIterator(LineQueue<E> queue){
			this.node = queue.peekFirst();
		}

		@Override
		public boolean hasNext() {
			return node != null;
		}

		@Override
		public E next() {
			E toReturn = node.getCurrent();
			node = node.getChild();
			return toReturn;
		}

		@Override
		public void remove() {
			throw new RuntimeException("Can not remove from Iterator");
		}
		
	}
	
	/**
	 * Iterator for iterating in descending order from last element to the first
	 * @author iMapster
	 *
	 */
	private class LineQueueDescendingIterator implements Iterator<E>, Iterable<E>{
		
		private LineQueuePosition<E> node;
		
		public LineQueueDescendingIterator(LineQueue<E> queue){
			this.node = queue.peekLast();
		}

		@Override
		public boolean hasNext() {
			return node != null;
		}

		@Override
		public E next() {
			E toReturn = node.getCurrent();
			node = node.getParent();
			return toReturn;
		}

		@Override
		public void remove() {
			throw new RuntimeException("Can not remove from Iterator");
		}

		@Override
		public Iterator<E> iterator() {
			return this;
		}
		
	}
}
