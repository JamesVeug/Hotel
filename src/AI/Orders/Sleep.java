package AI.Orders;

import java.awt.Color;

import AI.Guest;
import Hotel.FloatingText;
import Hotel.HotelMap;
import Hotel.MapPoint;

public class Sleep extends Order {

	long nextSnooze = 0;
	
	public Sleep(){
		this.nextSnooze = System.currentTimeMillis()+1000;
	}
	
	@Override
	public boolean execute(HotelMap map, Guest g) {
		// Check if not in bed
		if( !g.inBed() ){
			g.addNewOrder(new GoToBed());
		}
		
		
		
		// Not sleepy anymore
		if( g.getEmotions().Sleepiness == 0 ){
			g.vacate();
			return true;
		}
		
		// Should we snooze?
		if( System.currentTimeMillis() > nextSnooze ){
			g.getEmotions().Sleepiness--;
			
			FloatingText Z = new FloatingText("Z", new MapPoint(g.getPosition()), Color.white, 2);
			map.addFloatingText(Z);
			this.nextSnooze = System.currentTimeMillis()+1000;
		}
		
		return false;
	}

}
