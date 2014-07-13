package Objects;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import AI.AI;
import AI.Guest;
import Hotel.HotelMap;
import Hotel.MapPoint;



/**
 * Bed entity of which Guests are able to occupy and sleep in.
 * At the moment acting as a room and should eventually have maids clean them as apart of their jobs.
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class Bed extends Entity{

	private String roomNumber;
	public boolean canBeRented = true;
	private boolean dirty = false;
	public Guest guest = null;
	public long checkOutTime = 0;

	public ArrayList<AI> occupied = new ArrayList<AI>();

	public int cost = 100;

	// Not used
	public int angle = 270;

	/**
	 * Creates a new bed object at the given position
	 * @param point Position to create this bed in
	 */
	public Bed(String roomNumber, MapPoint point){
		this.setPosition(point);
        this.setRoomNumber(roomNumber);
		this.saveImage("resources/Entities/object/bed.png");
	}

	/**
	 * Returns the guest that is renting this room
	 * @return Guest renting the room if any otherwise null
	 */
	public Guest guest(){
		return guest;
	}

	public void occupy(Guest guest){
		if( occupied.contains(guest) ){
			System.out.println("WARNING: " + guest.name() + " already occupied by Bed");
		}
		occupied.add(guest);
		guest.occupiedBy(this);
		guest.setShouldDraw(false);
	}

	public void vacate(Guest guest){
		occupied.remove(guest);
		guest.occupiedBy(null);
		guest.setShouldDraw(true);

		// The bed is dirty now!
		if( !dirty ){
			dirty();
		}
	}

	/**
	 * Cleans the room
	 */
	public void clean(){
		dirty = false;
	}

	private void dirty(){
		dirty = true;
	}

	/**
	 *
	 * @param guest Guest to rent the room
	 * @param seconds Duration to book the room for
	 */
	public void rent(Guest guest, int seconds ){
		canBeRented = false;
		this.guest = guest;
		checkOutTime = System.currentTimeMillis()+(seconds*1000) ;
	}

	/**
	 * Returns a boolean if this bed is currently rented to a guest
	 * @return true if not currently rented
	 */
	public boolean rented(){ return !canBeRented; }

	/**
	 * Checkout time
	 * @return check out time
	 */
	public long getCheckOutTime() {
		return checkOutTime;
	}

	@Override
	public void draw(Graphics g, HotelMap map){
		if( !occupied.isEmpty() ){
			// Draw Occupied

			// Base
			g.drawImage(new ImageIcon("resources/Entities/object/bed_base_occupied.png").getImage(),
	    			position.getX(),
	    			position.getY(),
	    			size.width,
	    			size.height, null);

			// Pillows
			g.drawImage(new ImageIcon("resources/Entities/object/bed_pillows_occupied.png").getImage(),
	    			position.getX(),
	    			position.getY(),
	    			size.width,
	    			size.height, null);

			// Guests
			for(int i = 0; i < occupied.size(); i++){
				occupied.get(i).draw(g);
			}

			// Blanket
			g.drawImage(new ImageIcon("resources/Entities/object/bed_blanket_occupied.png").getImage(),
	    			position.getX(),
	    			position.getY(),
	    			size.width,
	    			size.height, null);

		}
		else if ( dirty ){
			// Draw Dirty

			// Base
			g.drawImage(new ImageIcon("resources/Entities/object/bed_base_dirty.png").getImage(),
	    			position.getX(),
	    			position.getY(),
	    			size.width,
	    			size.height, null);

			// Pillows
			g.drawImage(new ImageIcon("resources/Entities/object/bed_pillows_dirty.png").getImage(),
	    			position.getX(),
	    			position.getY(),
	    			size.width,
	    			size.height, null);

			// Blanket
			g.drawImage(new ImageIcon("resources/Entities/object/bed_blanket_dirty.png").getImage(),
	    			position.getX(),
	    			position.getY(),
	    			size.width,
	    			size.height, null);
		}
		else{
			super.draw(g, map);
		}
	}

	/**
	 * Checks if the room is dirty or not
	 * @return
	 */
	public boolean isDirty() {
		return dirty;
	}

	public String roomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

}
