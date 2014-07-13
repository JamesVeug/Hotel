package Hotel;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import AI.Guest;


public class GuestInfoPanel extends JPanel {
	
	private Guest guest = null;
	
	private static final int AVATAR_SIZE = 75;
	private static final int AVATAR_LIP = 25;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7148863569221879562L;
	
	public GuestInfoPanel(){
		super();
		this.setBackground(new Color(255,235,217));
		this.setSize(200,400);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		
		
		/** Draw Guests Face */
		
		// Border lip
		int[] avatarLip = { 
				(getWidth()-AVATAR_SIZE)/2,
				AVATAR_LIP
		};
		
		// Face Lip
		int[] faceLip;
		
		if( guest != null ){
			int[] temp = { (guest.getFaceImage().getIconWidth()/2), // X
						   (guest.getFaceImage().getIconHeight()/2) }; // Y
			faceLip = temp;
		}
		else{
			int[] temp = { 0,0 };
			faceLip = temp;
		}
		
		// Draw Face
		if( guest != null ){
			g.drawImage(guest.getFaceImage().getImage(), avatarLip[0]+AVATAR_SIZE/2-faceLip[0], avatarLip[1]+AVATAR_SIZE/2-faceLip[1], null);
		}
		
		// Draw Border
		g.setColor(Color.black);
		g.drawRect(avatarLip[0], avatarLip[1], AVATAR_SIZE, AVATAR_SIZE);
		
		/** Draw Information */
		g.setColor(Color.black);
		int x = AVATAR_LIP;
		int y = (2*AVATAR_LIP) + AVATAR_SIZE;
		
		// Name
		String name = guest != null ? guest.name() : "/";
		g.drawString("Name: " + name, x, y += 20);
		
		// State
		String order = guest != null ? guest.getCurrentOrder() != null ? guest.getCurrentOrder().getClass().getSimpleName() : "? " : "/";
		g.drawString("Order: " + order, x, y += 20);
		
		// Money
		String funds = guest != null ? "$" + guest.funds().toString() : "/";
		g.drawString("Funds: " + funds, x, y += 20);
		
		// Room Number
		String room = guest != null ? guest.getRoom() != null ? String.valueOf(guest.getRoom().roomNumber()) : "/" : "/";
		g.drawString("Room: " + room + "\n", x, y += 20);
		
		

		// Happiness
		String Happiness = "" + (guest != null ? guest.getEmotions().Happiness : "/");
		g.drawString("Happiness:  " + Happiness + " / 10", x, y += 20);
		
		
		String Hunger = "" + (guest != null ? guest.getEmotions().Hunger : "/");
		g.drawString("Hunger:         " + Hunger + " / 10", x, y += 20);
		
		
		String Bladder = "" + (guest != null ? guest.getEmotions().Bladder : "/");
		g.drawString("Bladder:        " + Happiness + " / 10", x, y += 20);
		
		
		String Sleepiness = "" + (guest != null ? guest.getEmotions().Sleepiness : "/");
		g.drawString("Sleepiness: " + Sleepiness + " / 10", x, y += 20);
	}
	
	public void setGuest(Guest guest){
		this.guest = guest;
	}

	public Guest guest() {
		return guest;
	}
}
