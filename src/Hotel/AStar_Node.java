package Hotel;
import Navigation.NavLink;
import Navigation.NavPoint;

/**
 * Node class for the Algorithm AStar used to hold all the information from one node to the end node.
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class AStar_Node implements Comparable<AStar_Node> {
	private AStar_Node parent;
	private NavPoint NavPoint;
	private double length;
	private NavLink path;

	// Distance in NavPoints form the start NavPoint
	private int depth = 0;

	private double estimate;

	/**
	 * Given a parent, child total length and estimate
	 * @param parent - NavPoint this came from
	 * @param NavPoint - This NavPoint
	 * @param totalLength
	 * @param estimate
	 */
	public AStar_Node(AStar_Node parent, NavPoint NavPoint, double length, double estimate, NavLink path) {
		this.parent = parent;
		this.NavPoint = NavPoint;
		this.length = length;
		this.estimate = estimate;
		this.setPath(path);

		this.setDepth(this.parent != null ? this.parent.getDepth()+1 : 0);
	}

	@Override
	public int compareTo(AStar_Node other) {

		int value = (int) Math.signum(this.getTotalLength() - other.getTotalLength());
		if( value == 0 ){
			// If the totals are the same
			// Return the lowest estimate
			return (int)(this.getLength() - other.getLength());
		}

		// Return who has the lowest estimate
		return value;
	}

	public AStar_Node getParent() {
		return parent;
	}

	public void setParent(AStar_Node start) {
		this.parent = start;
	}

	public NavPoint getNavPoint() {
		return NavPoint;
	}

	public void getNavPoint(NavPoint end) {
		this.NavPoint = end;
	}

	public double getTotalLength() {
		return (length + estimate);
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getEstimate() {
		return estimate;
	}

	public void setEstimate(double estimate) {
		this.estimate = estimate;
	}

	/**
	 * @return the size of the entire linked list
	 */
	public int getSize() {
		return parent == null ? 1 : parent.getSize() + 1;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public NavLink getPath() {
		return path;
	}

	public void setPath(NavLink path) {
		this.path = path;
	}

}
