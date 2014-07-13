package Navigation;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import Hotel.MapPoint;
import Hotel.Point3D;

/**
 * A Navigation 3D Point on the map that is able to be connected to other NavPoints by a NavLink for path finding.
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class NavPoint extends MapPoint{
	
	private final int ID;
	private ArrayList<NavPoint> connections = new ArrayList<NavPoint>();
	private HashMap<NavPoint,NavLink> links = new HashMap<NavPoint,NavLink>();
	
	public NavPoint(Point3D position) {
		super(position);
		this.ID = -1;
	}
	
	public NavPoint(int ID, Point3D position) {
		super(position);
		this.ID = ID;
	}

	public ArrayList<NavPoint> getConnections() {
		return connections;
	}
	
	/**
	 * Breaks the link between the two nodes symmetrically
	 * Returns if it was removed or not
	 * @param neighbour
	 * @return if the this node broke connection with it's neighbour (not neighbour with this node)
	 */
	public boolean breakLink(NavPoint neighbour){
		// Check if we are connected to the neighbour
		if( this.connections.contains(neighbour) ){
			
			// Unlink us to them
			this.connections.remove(neighbour);
			this.links.remove(neighbour);
			
			// Unlink them to us
			neighbour.breakLink(this);
			return true;
		}
		return false;
	}
	
	/**
	 * Links the two NavPoints together
	 * @param neighbour
	 */
	public void link(NavPoint neighbour) {
		if( !this.connections.contains(neighbour) ){
			this.connections.add(neighbour);
			
			NavLink link = new NavLink(this, neighbour);
			this.links.put(neighbour,link);
			neighbour.linkTo(this);
		}
	}
	
	/**
	 * Links this node to the given neighbour
	 * @param neighbour
	 * @return Link from this node to the neighbour node IF conencted, otherwise null
	 */
	public NavLink linkTo(NavPoint neighbour) {
		if( !this.connections.contains(neighbour) ){
			this.connections.add(neighbour);
			
			NavLink link = new NavLink(this, neighbour);
			this.links.put(neighbour,new NavLink(this,neighbour));
			return link;
		}
		return null;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(getX()+8, getY()+8,5,5);
	}

	/**
	 * Get all the links that this node is connected to anti-symmetrically
	 * @return a Collection of NavLinks from this node to the end node
	 */
	public Collection<NavLink> links() {
		return links.values();
	}

	/**
	 * Breaks all the links between this navPoint and it's connecting NavPoints
	 */
	public void clearLinks() {
		for(int i = connections.size()-1; i >= 0; i--){
			this.breakLink(connections.get(i));
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result += prime * ID;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NavPoint))
			return false;
		NavPoint other = (NavPoint) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	public int ID() {
		return ID;
	}

	public boolean isLinkedTo(NavPoint target) {
		return connections.contains(target);
	}
}
