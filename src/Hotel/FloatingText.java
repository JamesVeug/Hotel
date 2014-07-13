package Hotel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import Objects.Entity;



public class FloatingText extends Entity implements Thinkable {
	
	protected String text;
	protected Color colour;
	protected long expireyDate;
	
	/**
     * Constructor for objects of class AI
     */
    public FloatingText(String text, MapPoint point, Color colour, int lifeSeconds)
    {
        setPosition(point);
        this.text = text;
        this.expireyDate = System.currentTimeMillis() + (lifeSeconds*1000);
        this.colour = colour;
    }
    
    @Override
    public void think(HotelMap map){
    	this.position.translate(0, -0.1, 0);
    }

	@Override
	public void draw(Graphics g) {
		
		// Life = 1000 = 1
		// Current = 500 
		// Opacity = 
		float remaining = (expireyDate - System.currentTimeMillis())/1000;
		if( remaining > 0 ){
			FontMetrics metrics = g.getFontMetrics();
			int width =  metrics.stringWidth(text);
			int height = metrics.getHeight();
			
			g.setColor(colour);
			g.drawString(text,position.getX()-(width/2), position.getY()-(height/2));
			// Remaining in seconds
			//float opacity = (int)(remaining*100);
			//Graphics2D newGraphics = new Graphics2D
			//		(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		}//arg0
		//BufferedImage newImage = new BufferedImage(, arg1, arg2)
		//ImageIcon newIcon = drawImage.
	}

	public String text() {
		return text;
	}

	public long expireyDate() {
		return expireyDate;
	}
}
