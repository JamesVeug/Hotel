package Objects;
import java.util.UUID;
import java.util.ArrayList;
import java.awt.*;

import Hotel.HotelMap;
import Hotel.MapPoint;

/**
 * Final SuperClass to all objects/entities/sprites/tiles of which holes information such as a set position, unique ID and boolean if the entity requires to be draw or not.
 * 
 * @author James Veugelaers
 * @version 15/15/2014
 */
public abstract class ObjectBase
{
    protected MapPoint position = new MapPoint();
    protected String id = UUID.randomUUID().toString();
    protected boolean shouldDraw = true;
    
    public abstract void draw(Graphics g, HotelMap map);
    
    /**
     * Moves the 
     * @param x amount to move the x position by
     * @param y amount to move the y position by
     * @param z amount to move the z position by
     */
    public void moveBy(double x, double y, double z){ 
    	position.translate(x,y,z); 
    }

    
    public void setID(String newID){ this.id = newID; }
    public String getID(){ return this.id; }
    
    public MapPoint getPosition(){ return this.position; }
    public void setPosition(MapPoint p){ this.position = p; }
    
    /**
     * Returns an ArrayList of Strings that contain which should be saved from this class and it's super classes
     * 
     * MapPoint
     * {
     * 		( 10.0, 20.0, 0 )
     * }
     */
    public ArrayList<String> getSaveInfo(){
    	ArrayList<String> saveInfo = new ArrayList<String>();
        saveInfo.add("ID");
        saveInfo.add("{");
        saveInfo.add("\t" + this.getID());
        saveInfo.add("}");

        saveInfo.add("MapPoint");
        saveInfo.add("{");
        saveInfo.add("\t" + position);
        saveInfo.add("}");

        return saveInfo;
    };

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectBase other = (ObjectBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	/**
	 * Should we draw this entity? (True by default)
	 * @return true if we should draw
	 */
	public boolean shouldDraw() {
		return shouldDraw;
	}

	public void setShouldDraw(boolean shouldDraw) {
		this.shouldDraw = shouldDraw;
	}
}
