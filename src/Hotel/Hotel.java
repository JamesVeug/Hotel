package Hotel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import AI.AI;
import AI.Guest;
import AI.Staff;
import AI.Orders.LeaveHotel;
import Objects.Bed;
import Objects.Entity;


public class Hotel {

	static String[] layout = {
		"_____________________",
		"|B B B B B B B B B B|",
		"|                   |",
		"|B B B B   B B B   B|",
		"|________ _______ __|",
		"|B |                |",
		"|       __ __ __ __ |",
		"|__|    |  |  |  |  |",
		"|       | B| B| B| B|",
		"|       |__|__|__|__|",
		"|R                  |",
		"|       __ __ __ __ |",
		"|R      |  |  |  |  |",
		"|       | B| B| B| B|",
		"|___  __|__|__|__|__|",
		"x,,,,,,,,,,,,,,,,,,,x",
		",,,,,,,,,,,,,,,,,,,,,",
		",,,,x,,,,,,,,,,,,,,,,"
	};

	static GuestInfoPanel guestInfo;

	static HotelMap map = new HotelMap(layout);

	public static void main(String[] args){
		JFrame frame = new JFrame("Hotel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((map.getWidth()+15)*25,((map.getHeight()+4)*25));

		@SuppressWarnings("serial")
		final JPanel panel = new JPanel(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				map.draw(g);
				
			}
		};
		panel.addMouseListener(new MouseListener(){
			@Override public void mouseClicked(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {
				Guest g = map.getGuest(arg0.getX(), arg0.getY());
				//if( g != null ){
					if( guestInfo != null ){
						guestInfo.setGuest(g);
						map.setSelectedObject(g);
					}
				//}

				Entity entity = map.getEntity(arg0.getX(), arg0.getY());
				if( entity != null && entity instanceof Bed){
					map.cleanRoom((Bed) entity);
					return;
				}


			}
		});
		panel.setPreferredSize(new Dimension((map.getWidth()+2)*25, (map.getHeight()+4)*25));
		frame.add(panel,BorderLayout.WEST);

		guestInfo = new GuestInfoPanel();
		guestInfo.setSize(guestInfo.getWidth(),panel.getPreferredSize().height);
		guestInfo.setPreferredSize(guestInfo.getSize());
		frame.add(guestInfo,BorderLayout.EAST);

		/**
		 * MENU BAR
		 */
		JMenuBar bar = new JMenuBar();
		bar.setBorder(BorderFactory.createLineBorder(new Color(192,192,192)));

		JMenu view = new JMenu("View");
		for(Entry<String, Boolean> entry : map.getShouldDraws().entrySet()){
			final String key = entry.getKey();
			final String string = "Show " + key.substring(0,1).toUpperCase() + key.toLowerCase().substring(1);
			JMenuItem viewButton = new JMenuItem(string);
			viewButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					map.setDraw(key, !map.shouldDraw(key));
					panel.repaint();
					guestInfo.repaint();
				}

			});
			view.add(viewButton);
		}


		bar.add(view);
		frame.setJMenuBar(bar);

		frame.setVisible(true);

//		Tile exit = map.getExit();
//		Guest g2 = new Guest(new MapPoint(exit.getPosition()),"resources/Entities/object/AI/g.png");
//		map.addGuest(g2);
		
		int count = 0;
		long lastTime = System.currentTimeMillis();
		long create = System.currentTimeMillis();
		while (true) {
			
			if (System.currentTimeMillis() >= lastTime + 10) {
				// Add a new guest?
				Random rand = new Random();
				final int chance = 201;
				int random = rand.nextInt(chance);
				//if( random == chance-1 && count < 2000 ){
				if( System.currentTimeMillis() >= create ){
					// Get Random exit
					Tile exit = map.getExit();
					Guest guest = new Guest(new MapPoint(exit.getPosition()),"resources/Entities/object/AI/g.png");
					map.addGuest(guest);
					count++;
					create = System.currentTimeMillis() + 1000;
				}

				// Draw Guests
				for (int i = 0; i < map.getGuests().size(); i++) {
					Guest guest = map.getGuests().get(i);
					if( guest.getCurrentOrder() instanceof LeaveHotel && map.outsideHotel(guest.getPosition()) ){

						// Guest wanting to leave the hotel
						if( guestInfo.guest() == guest ){

							map.setSelectedObject(null);
							guestInfo.setGuest(null);
							guestInfo.repaint();
						}
						map.getGuests().remove(guest);
						count--;
					}
					else{
						guest.think(map);
					}
				}

				// Draw Staff
				for (int i = 0; i < map.getStaff().size(); i++) {
					Staff staff = map.getStaff().get(i);
					staff.think(map);
				}

				// Draw Sprites
				for (int i = 0; i < map.sprites().size(); i++) {
					FloatingText floater = map.sprites().get(i);
					floater.think(map);
				}
				panel.repaint();
				guestInfo.repaint();

				lastTime = System.currentTimeMillis();
			}
		}
	}
}
