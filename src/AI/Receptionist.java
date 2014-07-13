package AI;

import java.util.ArrayDeque;
import java.util.Queue;

import Hotel.HotelMap;
import Hotel.MapPoint;
import Objects.Bed;
import Objects.Entity;

public class Receptionist extends Staff implements Queueable<Guest> {

	private Guest guest = null;
	private MapPoint queuePosition;
	private ArrayDeque<Guest> guestQueue = new ArrayDeque<Guest>();

	private long nextTalkTime = 0;

	static int CHECKIN_TIME = 2;
	static int CHECKOUT_TIME = 2;

	/**
	 * Constructor for objects of class AI
	 */
	public Receptionist(MapPoint point, String image) {
		this.setPosition(point);
		this.saveImage(image);
		this.queuePosition = new MapPoint(this.position);
	}

	@Override
	public void think(HotelMap map) {
		if (guest != null) {

			// Check if we have finished talking to the guest
			if(System.currentTimeMillis() < nextTalkTime ){
				return;
			}

			// Does the guest have a room?
			if (guest.getRoom() == null) {

				// Check guest into a room
				checkIn(guest, map);
			}
			// We have a room and want to check out
			else if (guest.getRoom() != null) {

				// Check the guest OUT of a room
				checkOut(guest, map);
			}

			// Not talking to guest anymore
			guest = null;
			dequeue();

			// Not Talking to anyone
			nextTalkTime = 0;

		} else {
			Guest nextInLine = guestQueue.peekFirst();

			// Get the next person in the line
			if (nextInLine != null && this.canTalk(nextInLine)) {
				this.guest = nextInLine;//dequeue();
				nextTalkTime = System.currentTimeMillis()+1000;
			}
		}

	}

	/**
	 * Checks the given guest out of the hotel
	 *
	 * @param guest
	 * @param map
	 * @returns if the guest had a room and was checked out successfully
	 */
	public boolean checkOut(Guest guest, HotelMap map) {

		if (guest.getRoom() != null) {
			// Get the room being rented
			Bed room = guest.getRoom();

			// Remove guest from room
			room.guest = null;

			// Reset time
			room.checkOutTime = 0;

			// Allow the room to be rented
			room.canBeRented = true;

			// Clean the room // TEMPORARY
			room.clean(); // TEMPORARY

			// Set the guest to not be renting this room
			guest.setRoom(null);

			// Checked out
			return true;
		}

		// Not checked out
		return false;
	}

	/**
	 * Checks the given guest into the hotel Returns a boolean if they were
	 * checked in or not
	 *
	 * @param guest
	 * @param map
	 * @return true if checked in
	 */
	public boolean checkIn(Guest guest, HotelMap map) {

		Bed room = getAvaiableRoom(guest, map);
		if (room != null) {
			guest.setRoom(room);
			guest.spend(room.cost);
			room.rent(guest, 2);

			// Checked in
			map.sellRoom(room, guest);

			// Checked in successfully
			return true;
		}

		// Not checked in
		return false;
	}

	public Bed getAvaiableRoom(Guest guest, HotelMap map) {
		for (Entity e : map.getEntities()) {
			if (e instanceof Bed) {
				Bed b = (Bed) e;
				if (!b.isDirty() && !b.rented() && guest.canAfford(b.cost)) {
					return b;
				}
			}
		}

		// No rooms for the guest
		return null;
	}

	public void talkTo(Guest guest) {
		System.out.println("REMOVED CODEEEEEEEEEEEEe");
		// this.guest = guest;
	}

	public boolean canTalk(Guest guest) {
		return this.guest == null
				&& guest.getPosition().distance(queuePosition) < 1;
	}

	public Guest getGuest() {
		return guest;
	}

	/**
	 * Adds the given guest to the queue
	 *
	 * @param guest
	 */
	public void enqueue(Guest guest) {
		if( guestQueue.contains(guest) ){
			System.out.println("WARNING: Guest already in queue: " + guest.name());
			return;
		}
		
		guestQueue.add(guest);

		if( !guestQueue.isEmpty() ){
			/*System.out.print("Q ");
			for (Guest g : guestQueue) {
				System.out.print(g.name() + " <- ");
			}

			System.out.println();*/
		}


	}

	/**
	 * Dequeue from our queue of guests that want to talk to us
	 * @return
	 */
	public Guest dequeue(){
		Guest polled = guestQueue.poll();

		if( !guestQueue.isEmpty() ){
			System.out.print("D ");
			for (Guest g : guestQueue) {
				System.out.print(g.name() + " <- ");
			}


			System.out.println();
		}


		return polled;
	}

	public Guest getLastInQueue() {
		return guestQueue.peekLast();
	}

	public MapPoint queuePosition() {
		return this.queuePosition;
	}

	@Override
	public ArrayDeque<Guest> getQueue() {
		return guestQueue;
	}
}
