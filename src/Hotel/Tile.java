package Hotel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

import Objects.Entity;

/**
 * A Tile for the map of which is also connected to all the tiles surrounding it.
 * 
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class Tile extends Entity
{
    
    protected HashSet<Tile> neighbours = new HashSet<Tile>();
	
    public void addNeighbours(Collection<Tile> n){
    	neighbours.addAll(n);
    }

    /**
     * Constructor for objects of class AI
     */
    public Tile(Tile entityType, MapPoint... placedMapPoint)
    {
        super(entityType,placedMapPoint);
    }
    
    /**
     * Constructor for objects of class AI
     */
    public Tile(MapPoint point, String image)
    {
        this.setPosition(point);
        this.saveImage(image);
    }

    /**
     * When importing the entity list, this will assign all the properties for the entity
     * Scanner is also used to assing the entity buttons to our entity selection list found in /Entities.
     */
    public Tile(String D, Scanner scan){
        super(D,scan);
    }

    /**
     * CHANCE TO THROW AN EXCEPTION!
     */
	public void clearNeighbours() {
		for( Tile t : neighbours ){
			t.breakLink(this);
		}
		
	}
    
    /**
     * Breaks the link between this object and the given neighbour so neigher are connected to each other
     * @param neighbour
     */
    public void breakLink(Tile neighbour){
    	if( neighbours.contains(neighbour) ){
    		neighbours.remove(neighbour);
    		neighbour.breakLink(this);
    	}
    }
    
    /**
     * Link this tile to the the given neighbour, and the neighbour to this object
     * @param neighbour
     */
    public void linkNeighbour(Tile neighbour){
    	if( !neighbours.contains(neighbour) ){
    		neighbours.add(neighbour);
    		neighbour.linkNeighbour(this);
    	}    	
    }
}
