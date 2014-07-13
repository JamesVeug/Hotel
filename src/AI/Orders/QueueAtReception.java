package AI.Orders;

import AI.Guest;
import AI.Receptionist;
import AI.Staff;
import Hotel.HotelMap;

public class QueueAtReception extends Order{

	private Receptionist leastQueue = null;
	private boolean queued = false;
	
	@Override
	public boolean execute(HotelMap map, Guest guest) throws FailedOrderException {
		
		// Go to Reception
		if( leastQueue == null ){
			int length = Integer.MAX_VALUE;
			
			// Get the best receptionist
			for( Staff staff : map.getStaff() ){
				if( staff instanceof Receptionist ){
					
					// Look for closer path
					Receptionist receptionist = (Receptionist)staff;
					if( this.leastQueue == null || receptionist.getQueue().size() < length ){
						
						// Save Best Receptionist
						this.leastQueue = receptionist;
						length = receptionist.getQueue().size();
						
					}
				}
			}
		
			// Go to the talk position in the queue
			//this.leastQueue.enqueue(guest);
			guest.addNewOrder(new GoToDestination(guest.getPosition(), this.leastQueue.queuePosition()));
			return false;
		}
		
		if( queued == false ){
			
			// We are at the queue position, so queue up
			this.leastQueue.enqueue(guest);
			guest.addNewOrder(new Queue<Receptionist,Guest>(leastQueue));
			queued = true;
			return false;
		}
		
		
		// Finished queuing at reception		
		return true;
	}

}
