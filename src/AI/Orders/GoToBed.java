package AI.Orders;

import AI.Guest;
import Hotel.HotelMap;
import Hotel.MapPoint;

public class GoToBed extends Order{
	
	private boolean alreadyQueued = false;

	@Override
	public boolean execute(HotelMap map, Guest g) throws FailedOrderException {
		
		// We need a bed
		if( g.getRoom() == null && !alreadyQueued ){
			g.addNewOrder(new CheckIn());
			return false;
		}
		
		if( g.getRoom() == null && alreadyQueued ){
			// Can't get a bed
			return true;
		}
		
		// We are in bed so sleep
		MapPoint point = g.getRoom().getPosition();
		if( point.distance(g.getPosition()) == 0 ){
			
			// Get in Bed
			g.getRoom().occupy(g);
			return true;
		}
		
		// Go to Bed
		g.addNewOrder(new GoToDestination(g.getPosition(), point));
		return false;
	}

}
