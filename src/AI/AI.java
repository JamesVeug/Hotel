package AI;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.ImageIcon;

import Hotel.Algorithms;
import Hotel.HotelMap;
import Hotel.MapPoint;
import Hotel.Thinkable;
import Navigation.NavPoint;
import Objects.Entity;
import Objects.ObjectBase;


/**
 * An abstract class acting as the super class for all AI subclass that require the method think.
 *
 * @author James Veugelaers
 * @version 15/15/2014
 */
public abstract class AI extends Entity implements Thinkable
{
	protected ObjectBase target = null;
    protected LinkedList<NavPoint> path = null;
    protected States state;
    protected ImageIcon faceImage;

    public enum States{
    	// Receptionist
    	RECEPTION,

    	// GUEST
    	HAPPY,
    	SLEEPY,
    	SLEEPING,
    	LEAVING,
    	QUEUEING,

    	//ZOMBIE
    	BRAINS;


    	public String toString(){
    		String lower = super.toString().toLowerCase();
    		return super.toString().substring(0, 1) + lower.substring(1);
    	}
    }

    public States state(){
    	return this.state;
    }

    public void setState(States newState){
    	this.state = newState;
    }

    /**
     * Gets a possible path from  the starting MapPoitn to the targetPoint
     * @param map
     * @param start
     * @param target
     */
    protected void setPath(HotelMap map, MapPoint start, MapPoint target){
    	NavPoint navStart = map.getNavigation().getClosestNode(start.X(), start.Y());
		NavPoint navEnd = map.getNavigation().getClosestNode(target.X(), target.Y());

		path = Algorithms.A_Star(navStart, navEnd);
    }

    public ImageIcon getFaceImage(){
    	return faceImage;
    }
}
