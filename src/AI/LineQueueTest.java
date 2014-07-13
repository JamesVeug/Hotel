package AI;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class LineQueueTest {
	
	private String[] array = { "First", "Second", "Third", "Forth", "Fifth", "Sixth", "Seventh", "Eighth", "Nineth", "Tenth" };
	private LineQueue<String> queue;

	@Before
	public void fill(){
		queue = new LineQueue<String>();
		for( String s : array ){
			queue.add(s);
		}
	}
	
	@Test
	public void testDescendingIterator() {
		
		int i = queue.size()-1;
		for(String s : queue.descendingIterator()){
			System.out.println("Descending - " + s);
			assertTrue(s.equals(array[i--]));
		}
	}
	
	@Test
	public void testAdd() {
		queue = new LineQueue<String>();
		
		// First Element
		String first = "FIRST";
		queue.add(first);
		
		assertTrue( queue.peekFirst().getParent() == null );
		assertTrue( queue.peekFirst().getCurrent() == first );
		assertTrue( queue.peekFirst().getChild() == null );
		
		// Second Element
		String second = "SECOND";
		queue.add(second);
		
		assertTrue( queue.peekFirst().getParent() == null );
		assertTrue( queue.peekFirst().getCurrent() == first );
		assertTrue( queue.peekFirst().getChild() != null );
		assertTrue( queue.peekFirst().getChild().getCurrent() == second );
		assertTrue( queue.peekFirst().getChild().getChild() == null );

		assertTrue( queue.peekLast() != null );
		assertTrue( queue.peekLast().getChild() == null );
		assertTrue( queue.peekLast().getCurrent() == second );
		assertTrue( queue.peekLast().getParent() != null );
		assertTrue( queue.peekLast().getParent().getCurrent() == first );
		assertTrue( queue.peekLast().getParent().getParent() == null );
	}
	
	@Test
	public void testAddLast() {
		queue = new LineQueue<String>();
		
		// First Element
		String first = "FIRST";
		queue.addLast(first);
		
		assertTrue( queue.peekFirst().getParent() == null );
		assertTrue( queue.peekFirst().getCurrent() == first );
		assertTrue( queue.peekFirst().getChild() == null );
		
		// Second Element
		String second = "SECOND";
		queue.addLast(second);
		
		assertTrue( queue.peekFirst().getParent() == null );
		assertTrue( queue.peekFirst().getCurrent() == first );
		assertTrue( queue.peekFirst().getChild() != null );
		assertTrue( queue.peekFirst().getChild().getCurrent() == second );
		assertTrue( queue.peekFirst().getChild().getChild() == null );

		assertTrue( queue.peekLast() != null );
		assertTrue( queue.peekLast().getChild() == null );
		assertTrue( queue.peekLast().getCurrent() == second );
		assertTrue( queue.peekLast().getParent() != null );
		assertTrue( queue.peekLast().getParent().getCurrent() == first );
		assertTrue( queue.peekLast().getParent().getParent() == null );
	}
	
	@Test
	public void addFirst(){
		queue = new LineQueue<String>();
		String first = "FIRST";
		String second = "SECOND";
		String third = "THIRD";
		
		queue.addFirst(first);
		assertTrue(queue.peekFirst().getCurrent() == first );
		assertTrue(queue.peekLast().getCurrent() == first );
		
		queue.addFirst(second);
		assertTrue(queue.peekFirst().getCurrent() == second );
		assertTrue(queue.peekFirst().getChild().getCurrent() == first );
		
		// Reverse
		assertTrue(queue.peekLast().getCurrent() == first );
		assertTrue(queue.peekLast().getParent().getCurrent() == second );

		queue.addFirst(third);
		assertTrue(queue.peekFirst().getCurrent() == third );
		assertTrue(queue.peekFirst().getChild().getCurrent() == second );
		assertTrue(queue.peekFirst().getChild().getChild().getCurrent() == first );
		
		// Reverse
		assertTrue(queue.peekLast().getCurrent() == first );
		assertTrue(queue.peekLast().getParent().getCurrent() == second );
		assertTrue(queue.peekLast().getParent().getParent().getCurrent() == third );
	}
	
	@Test
	public void pop(){
		System.out.println("Pop size: " + queue.size());		
		
		for(String s : queue){
			System.out.println("B " + s);
		}
		
		assertTrue(queue.peekFirst().getCurrent().equals(array[0]));
		LineQueuePosition<String> popped = queue.pop();
		
		// Display
		for(String s : queue){
			System.out.println("A " + s);
		}
		
		assertTrue(queue.peekFirst().getCurrent().equals(array[1]));
	}
	
	@Test
	public void testContains(){
		queue = new LineQueue<String>();
		String first = "FIRST";		
		
		
		queue.add(first);
		assertTrue(queue.peekFirst().getCurrent().equals(first));
		assertTrue(queue.peekFirst().getCurrent() == first);
		assertTrue(queue.contains(first));		

		queue = new LineQueue<String>();
		for(String s : array){
			queue.add(s);
			
			for( int x = 0; x < array.length; x++){
				if( x < queue.size() ){
					assertTrue(queue.contains(array[x]));
				}
				else{
					assertFalse(queue.contains(array[x]));
				}
			}
		}
	}
	
	@Test
	public void testRemove() {
		//int[] k = new int[10];
		//ArrayList<LineQueuePosition<String>> saved = new ArrayList<LineQueuePosition<String>>();
		
		for(String s : array){
			System.out.print(s + " ");
		}System.out.println();
		
		
		// Remove second element
		LineQueuePosition<String> node = queue.peekFirst().getChild();
		System.out.println(node.getCurrent());
		boolean b = queue.remove(node.getCurrent());

		System.out.println("Contains: " + queue.contains(node.getCurrent()));
		System.out.println("Removed: " + b);
		for(String s : array){
			System.out.print(s + " ");
		}System.out.println();
		
		// Ascending
		assertTrue(queue.contains(array[0]));
		assertFalse(queue.contains(array[1]));
		assertTrue(queue.contains(array[2]));
		assertTrue(queue.contains(array[3]));
	}
}
