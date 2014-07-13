package Hotel;


/**
 * A 3D Point in space consisting of x,y,z stored as doubles but can be recieved as ints.
 * Created due to getting annoyed at the java Point classes
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class Point3D {
	
	private double x;
	private double y;
	private double z;
	
	/**
	 * Creates a 3D point from the given x,y,z coordinates
	 * @param x position of the Point
	 * @param y position of the Point
	 * @param z position of the Point
	 */
	public Point3D(double x, double y, double z) {
		setLocation(x, y, z);
	}

	/**
	 * Assigns the position of the point to the given x,y,z coordinates
	 * @param x position of the Point
	 * @param y position of the Point
	 * @param z position of the Point
	 * @return a reference to this point
	 */
	public Point3D setLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Translates the point by the given x,y,z amount.
	 * @param x amount to move this point by
	 * @param y amount to move this point by
	 * @param amount to move this point by
	 * @return a reference to this point
	 */
	public Point3D translate(double x, double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	/**
	 * Get the distance form this point to the destination point
	 * @param destination
	 * @return distance in double from the pythagorus theorem into getting the distance
	 */
	public double distance(Point3D destination){
		return Math.sqrt(Math.pow(destination.x - x,2)+ 
				Math.pow(destination.y - y,2)+Math.pow(destination.z - z,2));
	}

	/**
	 * Returns a int casted version of this points x values.
	 * @return x as an int
	 */
	public int getX() {
		return (int)x;
	}

	/**
	 * Returns a int casted version of this points y values.
	 * @return y as an int
	 */
	public int getY() {
		return (int)y;
	}
	
	/**
	 * Returns a int casted version of this points z values.
	 * @return z as an int
	 */
	public int getZ() {
		return (int)z;
	}
	
	public double X() {
		return x;
	}

	public double Y() {
		return y;
	}
	
	public double Z() {
		return z;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (!(obj instanceof Point3D))
			return false;
		Point3D other = (Point3D) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

}
