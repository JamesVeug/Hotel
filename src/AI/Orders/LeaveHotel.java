package AI.Orders;

import AI.Guest;
import Hotel.HotelMap;
import Hotel.Tile;

public class LeaveHotel extends Order {

	private Tile exit = null;
	
	
	@Override
	public boolean execute(HotelMap map, Guest g) throws FailedOrderException {
		
		// Get Random Exit
		if( exit == null ){
			exit = map.getExit();
			
			// Go to the exit location
			g.addNewOrder(new GoToDestination(g.getPosition(), exit.getPosition()));
			return false;
		}
		
		// Are we on the tile?
		if( exit.getPosition().distance(g.getPosition()) == 0 ){
			System.out.println("FINISHED!");
			// Left the hotel
			return true;
		}
		
		// Have not left the hotel yet
		return false;
	}

}
