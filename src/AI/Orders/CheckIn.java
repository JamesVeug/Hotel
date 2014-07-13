package AI.Orders;

import AI.Guest;
import Hotel.HotelMap;

public class CheckIn extends Order {
	
	boolean queued = false;
	
	@Override
	public boolean execute(HotelMap map, Guest g) throws FailedOrderException {
		
		// We have a bed
		if( g.getRoom() != null ){
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
