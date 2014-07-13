package Navigation;
import java.awt.*;

import Hotel.MapPoint;

/**
 * Write a description of class WayPoint here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WayPoint
{
    private MapPoint position;
    private Dimension size = new Dimension(5,5);
    
    /**
     * Constructor for objects of class WayPoint
     */
    public WayPoint(MapPoint pos)
    {
        this.position = pos;
    }

    public void draw(Graphics g, boolean shouldDraw)
    {
        g.setColor(Color.white);
        g.fillOval((int)(position.getX()-(size.getWidth()/2)), 
        		(int)(position.getY()-(size.getHeight()/2)), 
        		(int)size.getWidth(), 
        		(int)size.getHeight());

    }
    
    public void setPosition(MapPoint pos){
        this.position = pos;
    }
    
    public MapPoint getPosition(){
        return this.position;
    }
    
    public boolean on(MapPoint pos){
        return pos.distance(getPosition()) <= this.size.getWidth();
    }
}
