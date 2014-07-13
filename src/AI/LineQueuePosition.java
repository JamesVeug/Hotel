package AI;

public class LineQueuePosition<E>{
	private LineQueuePosition<E> parent;
	private E current;
	private LineQueuePosition<E> child;
	
	public LineQueuePosition(LineQueuePosition<E> parent,E current, LineQueuePosition<E> child){
		this.setParent(parent);
		this.setCurrent(current);
		this.setChild(child);
	}
	
	public boolean equals(Object o){
		if( o == null || current == null ) return false;
		
		return current.equals(o);
	}
	
	public int hashCode(){
		if( current == null ) return 0;		
		return current.hashCode();
	}

	public LineQueuePosition<E> getParent() {
		return parent;
	}

	public void setParent(LineQueuePosition<E> parent) {
		this.parent = parent;
	}

	public LineQueuePosition<E> getChild() {
		return child;
	}

	public void setChild(LineQueuePosition<E> child) {
		this.child = child;
	}

	public E getCurrent() {
		return current;
	}

	public void setCurrent(E current) {
		this.current = current;
	}
}
