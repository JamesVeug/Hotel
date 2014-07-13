package AI.Orders;

import java.util.LinkedList;

import AI.Guest;
import Hotel.Algorithms;
import Hotel.HotelMap;
import Hotel.MapPoint;
import Navigation.NavPoint;

public class GoToDestination extends Order implements Extras<MapPoint>{

	private final MapPoint[] extras;
	private LinkedList<NavPoint> path = null;
	
	public GoToDestination(MapPoint start, MapPoint end) {
		
		// Need start and end loctaqions when declaring a goto Destination Order
		if( start == null ){
			fail("Invalid instatiation with GoToDestination, start is null");
		}
		else if( end == null ){
			fail("Invalid instatiation with GoToDestination, end is null");
		}
		
		this.extras = new MapPoint[]{ start, end };
	}

	@Override
	public MapPoint[] extras() {
		return extras;
	}

	@Override
	public boolean execute(HotelMap map, Guest g) throws FailedOrderException {
		MapPoint start = extras[0];
		MapPoint destination = extras[1];
		
		// Get path to destination
		if( path == null ){
			
			// Go to reception
			NavPoint navStart = map.getNavigation().getClosestNode(start.X(), start.Y());
			NavPoint navEnd = map.getNavigation().getClosestNode(destination.X(), destination.Y());
			path = Algorithms.A_Star(navStart, navEnd);
			return false;
		}
		
		
		// Check path
		if( start.distance(destination) == 0 ){
			
			// At Destination
			return true;
		}
		else if( path.getFirst().distance(extras[0]) == 0 ){
			
			// At next position, remove from linked list
			path.removeFirst();
		}
		
		
		// Move towards the destination
    	g.stepTowardsPosition(path.getFirst(), map);
		return false;
	}
	
	

}
