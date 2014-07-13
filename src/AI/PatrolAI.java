package AI;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;

import Hotel.HotelMap;
import Hotel.MapPoint;
import Navigation.WayPoint;

/**
 * Write a description of class AI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PatrolAI extends AI
{
    private List<WayPoint> WayPointList = new ArrayList<WayPoint>();
    private int nextWayPoint = 0;

    public PatrolAI(){}

    /**
     * Constructor for objects of class AI
     */
    public PatrolAI(PatrolAI entityType, MapPoint... placedMapPoint)
    {
        //super(entityType,placedMapPoint);
        this.WayPointList = entityType.getWayPoints();
    }

    /**
     * When importing the entity list, this will assign all the properties for the entity
     * Scanner is also used to assign the entity buttons to our entity selection list found in /Entities.
     */
    public PatrolAI(String D, Scanner scan)
    {
        while(scan.hasNext())
        {
            String target = scan.next();
            if(target.equals("}"))
            {
                // No more to add into this scanner
                break;
            }
            scan.next(); // {

            String directory = scan.next();
            if(target.equalsIgnoreCase("Image"))
            {
                this.saveImage(directory);
            }
            else if(target.equalsIgnoreCase("ID"))
            {
                this.setID(directory);
            }
            else if(target.equalsIgnoreCase("MapPoint"))
            {
                this.setPosition(new MapPoint(Double.valueOf(directory),scan.nextDouble(),scan.nextDouble()));
            }
            else if(target.equalsIgnoreCase("WayPoints"))
            {
                this.addWayPoint(new MapPoint(Double.valueOf(directory),scan.nextDouble(),scan.nextDouble()));
                while( !scan.hasNext("}") ){
                    this.addWayPoint(new MapPoint(scan.nextDouble(),scan.nextDouble(),scan.nextDouble()));
                }
            }
            else
            {
                this.putProperty(target,directory);
            }

            scan.next(); // }
        }
    }

    public void addWayPoint(MapPoint newPoint){
        if( newPoint == null ) return;

        // If we don't have any waypoints, then add a waypoint to our start position
        if(this.getWayPoints().isEmpty())
            WayPointList.add(new WayPoint(this.getPosition()));

        WayPointList.add(new WayPoint(newPoint));
    }

    public List<WayPoint> getWayPoints(){
        return WayPointList;
    }

    public void think(HotelMap map){
    	System.out.println("Think");
        if( !this.getWayPoints().isEmpty() ){
        	System.out.println("  True");
            // Assume there are a minimum of 2 WayPoints
            if(this.getPosition().distance(this.getWayPoints().get(nextWayPoint).getPosition()) == 0){
                // We are ontop of a WayPoint
                nextWayPoint = nextWayPoint+1 < this.getWayPoints().size() ? nextWayPoint+1 : 0;
            	System.out.println("    True");
            }
            else{
            	System.out.println("    Else");
                // We need to move towards the point
                int WPx = this.getWayPoints().get(nextWayPoint).getPosition().getX();
                int WPy = this.getWayPoints().get(nextWayPoint).getPosition().getY();

                int x = this.getPosition().getX() == WPx ? 0 : this.getPosition().getX() > WPx ? -1 : 1;
                int y = this.getPosition().getY() == WPy ? 0 : this.getPosition().getY() > WPy ? -1 : 1;
                this.moveBy(x,y,0);
            }
        }
    }

    /**
     * Returns a string of the following information for the selection type
     */
    public ArrayList<String> getSaveInfo()
    {
        ArrayList<String> saveInfo = super.getSaveInfo();
        if( !this.WayPointList.isEmpty() )
        {
            saveInfo.add("WayPoints");
            saveInfo.add("{");
            for(int i=1; i < this.getWayPoints().size(); i++){
                saveInfo.add("\t" + this.getWayPoints().get(i).getPosition());
            }
            saveInfo.add("}");
        }

        return saveInfo;
    }

    public void setPosition(MapPoint pos){ 
        super.setPosition(pos);
    }

    /**
     * Determines if the ouse is on the AI wheather the liveView is on or not
     */
    public boolean on(double x, double y){ 
        return ( x >= getPosition().getX() && x <= (getPosition().getX()+width()) && 
        		y >= getPosition().getY() && y <= (getPosition().getY()+height()) );
    }

    public Rectangle2D getBounds(){ 
        return new Rectangle2D.Double(position.getX(),position.getY(),width(), height());
    }
}
