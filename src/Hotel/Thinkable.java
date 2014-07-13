package Hotel;

/**
 * Interface indicating that the class will be a thinkable class
 * @author iMapster
 *
 */
public interface Thinkable {
	/**
	 * The method run to determine what the AI is determining what to do next
	 * @param map
	 */
	public void think(HotelMap map);
}
