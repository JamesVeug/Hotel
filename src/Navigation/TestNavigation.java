package Navigation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import AI.DummyAI;
import AI.Zombie;
import Hotel.GuestInfoPanel;
import Hotel.HotelMap;
import Hotel.MapPoint;
import Hotel.Tile;


public class TestNavigation {
	
	static String[] layout = { 
		"_____________________",
		"|                   |",
		"|                   |",
		"|                   |",
		"|                   |",
		"|                   |",
		"|                   |",
		"|                   |",
		"|                   |",
		"|                   |",
		"|                   |",
		"|                   |",
		"|         Z         |",
		"|___________________|",
	};
	
	static GuestInfoPanel guestInfo;

	static HotelMap map = new HotelMap(layout);
	
	public static void main(String[] args){
		JFrame frame = new JFrame("Backpacking, oh yes!");
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
				
				MapPoint clickPoint = new MapPoint((int)(arg0.getX()/25)*25,(int)(arg0.getY()/25)*25, 0);
				if( arg0.getButton() == MouseEvent.BUTTON1 ){
					
					
					for(Zombie z : map.zombies){
						z.setTarget(new DummyAI(clickPoint));
					}
				}else if( arg0.isMetaDown() ){
					Tile t = map.getTile(clickPoint.getX()/25, clickPoint.getY()/25);
					if( t.getPosition().getZ() == 1 ){
						Tile newTile =  map.getLayoutTile(' ', clickPoint.getX(), clickPoint.getY());
						map.addTile(newTile);
						map.getNavigation().getClosestNode(newTile.getPosition().X(), newTile.getPosition().Y()).setZ(t.getPosition().Z());
					}
					else{
						Tile newTile =  map.getLayoutTile('#', clickPoint.getX(), clickPoint.getY());
						map.addTile(newTile);
						map.getNavigation().getClosestNode(newTile.getPosition().X(), newTile.getPosition().Y()).setZ(t.getPosition().Z());
					}
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
		
		long lastTime = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() >= lastTime + 10) {
				
				// Draw Guests
				for (int i = 0; i < map.zombies.size(); i++) {
					Zombie z = map.zombies.get(i);
					z.think(map);
				}
				
				panel.repaint();
				guestInfo.repaint();

				lastTime = System.currentTimeMillis();
			}
		}
	}
}
