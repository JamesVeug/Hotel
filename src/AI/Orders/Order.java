package AI.Orders;

import AI.Guest;
import Hotel.HotelMap;

/**
 * Order class to represent the type of order in order to be performed by an AI
 * @author veugeljame
 */
public abstract class Order {
	
	/**
	 * Method of which should execute the order following instrcutions to the supplied guest
	 * @param map HotelMap of which contains all data from the map
	 * @param g Guest to order
	 * @return boolean if execution has finished or not.
	 */
	public abstract boolean execute(HotelMap map, Guest g) throws FailedOrderException;

	/**
	 * Compares the class orders class to what it should be, to reduce code length
	 * @param o
	 * @return
	 */
	public boolean is(Class<? extends Order> o){
		return o == this.getClass();
	}
	
	/**
	 * Fail the order with a message description
	 * @param message
	 */
	public void fail(String text){
		throw new FailedOrderException(text);
	}
}
