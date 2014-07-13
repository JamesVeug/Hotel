package AI.Orders;

import AI.Guest;
import Hotel.HotelMap;

public class CheckOut extends Order {
	
	boolean queued = false;
	
	@Override
	public boolean execute(HotelMap map, Guest g) throws FailedOrderException {
		
		// We still have a room
		if( g.getRoom() == null ){
			return true;
		}
		
		// Queue at Reception
		if( queued == false ){
			g.addNewOrder(new QueueAtReception());
			queued = true;
		}
		
		// Haven't got a bed yet
		return false;
	}
}
