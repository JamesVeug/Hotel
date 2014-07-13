package AI.Orders;

import AI.Guest;
import AI.Queueable;
import Hotel.HotelMap;

public class Queue<R extends Queueable<G>, G> extends Order{
	private R extra = null;
	
	public Queue(R extra){
		
		if( extra == null ){
			fail("Invalid Initialization for Queue: " + extra);
		}
		
				
		this.extra = extra;
	}
	
	@Override
	public boolean execute(HotelMap map, Guest g) throws FailedOrderException {
		
		if( !extra.getQueue().contains(g) ){
			return true;
		}
		
		//System.out.println("WREGTIWHEGIWHEGIGHE");
		return false;
	}

}
