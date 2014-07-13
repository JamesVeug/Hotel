package AI.Orders;


public class FailedOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2578080168863812724L;
	
	private final String text;
	public FailedOrderException(String text){
		super("FAILED ORDER: " + text);
		this.text = text;
	}
	
	/**
	 * Returns the emssage given by this exception
	 * @return
	 */
	public String message(){
		return text;
	}
}
