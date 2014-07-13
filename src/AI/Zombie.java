package AI;
import java.util.LinkedList;
import java.awt.*;

import Hotel.Algorithms;
import Hotel.HotelMap;
import Hotel.MapPoint;
import Hotel.Point3D;
import Navigation.NavPoint;
import Objects.ObjectBase;

/**
 * Write a description of class AI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Zombie extends AI
{
    private ObjectBase target = null;
    private LinkedList<NavPoint> path;
    
    /**
     * Constructor for objects of class AI
     */
    public Zombie(MapPoint point, String image)
    {
        this.setPosition(point);
        this.saveImage(image);
    }

    @Override
    public void think(HotelMap map){
    	//System.out.println("Think");
        if( target != null ){
        	//System.out.println("  No target");
        	if( path == null || path.isEmpty() || !canMoveTo(path.getFirst())   ){
        		NavPoint start = map.getNavigation().getClosestNode(this.getPosition().X(), this.getPosition().Y());
        		NavPoint end = map.getNavigation().getClosestNode(target.getPosition().X(), target.getPosition().Y());
        		
        		path = Algorithms.A_Star(start, end);
        		if( path == null){
        			//System.out.println("    No path found");
        		}
        	}
        	else{
        	
	        	Point3D point = path.get(0);
            	//System.out.println("    Following Path" + this.getPosition() + " -> " + point);
	        	double x = point.X()-this.getPosition().X();
	        	double y = point.Y()-this.getPosition().Y();
	        	double z = point.Z()-this.getPosition().Z();
	        	
	        	if( position.distance(point) == 0 ){
	        		// Too close to old position so get rid of it and find the next position
	        		if( path.getFirst().Z() != position.Z() ){ moveBy(0,0, (path.getFirst().Z() - position.Z())); }
	        		path.remove(0);
	        	}
	        	else{
	        		// Move towards the next position
		        	moveBy(Math.signum(x), Math.signum(y),z/25);
	        	}
        	}
        }
    }

    private boolean canMoveTo(NavPoint first) {
		return position.getZ() == first.getZ();
	}

	@Override
    public void draw(Graphics g, HotelMap map){
    	if( !shouldDraw ){ return; }
    	
    	// Draw pathing if we are allowed
    	if( map.shouldDraw("PATHING") && path != null && !path.isEmpty() ){
    		g.setColor(Color.blue);
    		g.drawLine((int)path.getFirst().getX()+10, (int)path.getFirst().getY()+10, 
    				(int)getPosition().getX()+10, (int)getPosition().getY()+10 );
    	
	    	for(int i = 0; i < path.size()-1; i++){
	    		NavPoint a = path.get(i);
	    		NavPoint b = path.get(i+1);
	    		
	    		g.drawLine(a.getX()+10, a.getY()+10, 
	    				   b.getX()+10, b.getY()+10 );	
	    	}
    	}
    	
    	// Draw image
        super.draw(g, map);
    	
    	if( target != null && target instanceof DummyAI ){
    		target.draw(g, map);
    	}
    	
    	// Draw image
        super.draw(g, map);
    }

	public ObjectBase getTarget() {
		return target;
	}

	public void setTarget(ObjectBase target) {
		this.target = target;
	}
}
