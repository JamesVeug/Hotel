package Navigation;
import java.awt.Color;
import java.awt.Graphics;


public class NavLink {
	
	private int ID = -1;
	private NavPoint start;
	private NavPoint end;
	
	public NavLink(NavPoint start, NavPoint end) {
		this.setStart(start);
		this.setEnd(end);
	}
	
	public NavLink(int ID, NavPoint start, NavPoint end) {
		this.setStart(start);
		this.setEnd(end);
	}

	public NavPoint getStart() {
		return start;
	}

	public void setStart(NavPoint start) {
		this.start = start;
	}

	public NavPoint getEnd() {
		return end;
	}

	public void setEnd(NavPoint end) {
		this.end = end;
	}

	public double length() {
		return start.distance(end);
	}
	
	public void draw(Graphics g){
		g.setColor(Color.GREEN);
		g.drawLine(start.getX()+10, start.getY()+10, 
				   end.getX()+10, end.getY()+10);
	}

	public int ID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		if (!(obj instanceof NavLink))
			return false;
		NavLink other = (NavLink) obj;
		if (ID != other.ID)
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
}
