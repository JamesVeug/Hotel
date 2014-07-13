package AI;
import java.awt.*;

import Hotel.HotelMap;
import Hotel.MapPoint;

/**
 * Doesn't do anything
 * 
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class DummyAI extends AI
{
    /**
     * Constructor for objects of class AI
     */
    public DummyAI(MapPoint point){
        this.setPosition(point);
    }
    
    /**
     * Think for a dummy AI performs no actions
     */
    @Override
    public void think(HotelMap map){
    }

    /**
     * Draws the DummyAI as an outlined oval at it's given position
     */
    @Override
    public void draw(Graphics g, HotelMap map){
    	g.setColor(Color.black);
    	g.drawOval(position.getX()+2,
    				position.getY()+2,
    				22,
    				22);
    }
}
