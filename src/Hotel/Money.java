package Hotel;

public class Money {
	public long value = 0;
	public Money(int money){
		this.value = money;
	}
	
	/**
	 * Returns the value of the money along with a splitting of every 3'rd value form the right to the list by a comma then adding an addutional .00 at the end as we don't deal with cents.
	 */
	public String toString(){
		String text = String.valueOf(value);
		int index = text.length()-3;
		while( index > 0 ){
			text = text.substring(0,index) + "," + text.substring(index);
			index -=3;
		}
		return text + ".00";
	}
}
