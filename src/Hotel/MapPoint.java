package Hotel;
/**
 * A MapPoint of which extends Point3D and contains helper methods
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class MapPoint extends Point3D implements Comparable<MapPoint> 
{

    /**
     * Constructs a new MapPoint with the coordinates 0,0,0
     */
    public MapPoint(){
        super(0,0,0);
    }
    
    /**
     * Constructs a new MapPoint with the given x,y,z coordinates 
     * */
    public MapPoint(double x, double y, double z){
    	super(x,y,z);
    }
    
    /**
     * Constructs a new MapPoint with the same coordinates as the given Point
     */
    public MapPoint(Point3D newPoint){
    	super(newPoint.X(), newPoint.Y(), newPoint.Z());
    }

    /**
     * Moves the point to the given x,y,z coordinates
     * Returns this node
     * @param moveByX
     * @param moveByY
     * @param moveByZ
     * @return - Returns the MapPoint after the performed move
     */
    @Override
    public MapPoint setLocation(double x, double y, double z){
        super.setLocation(x,y,z);
        return this;
    }
    
    /**
     * Moves the point by x,y,z coordinates
     * Returns this node
     * @param moveByX
     * @param moveByY
     * @return - Returns the MapPoint after the performed move
     */
    @Override
    public MapPoint translate(double moveByX, double moveByY, double moveByZ){
        super.translate(moveByX, moveByY, moveByZ);
        return this;
    }

    /**
     * Returns the coordinates of the point (x,y)
     */
    public String toString(){
        return ("(" + X() + "," + Y() + "," + Z() + ")");
    }
    
    /**
     * Returns (this.getX() - compare.getX()) + (this.getY() - compare.getY())
     * So lowest X,y is closer to the topleft of the zoomMap
     */
    public int compareTo(MapPoint compare){
        return (int)((X() - compare.X()) + (Y() - compare.Y()) + (Z() - compare.Z()));
    }
}
