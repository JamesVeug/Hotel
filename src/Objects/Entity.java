package Objects;
import java.awt.*;
import java.util.*;
import java.io.File;

import javax.swing.ImageIcon;

import Hotel.HotelMap;
import Hotel.MapPoint;

import java.awt.geom.Rectangle2D;

public class Entity extends ObjectBase
{
    protected File entityFile;
    protected Dimension size;
    protected ImageIcon drawImage;

    // All extra properties for the entity such as "nextLevel" for doors
    protected Map<String,String> properties = new HashMap<String,String>();

    public Entity(){}

    /**
     * When importing the entity list, this will assign all the properties for the entity
     * Scanner is also used to assign the entity buttons to our entity selection list found in /Entities.
     * 
     *Class
     *{ 
     *      Key 
     *      {
     *			Property
     *      }
     *      .
     *      .
     *      .
     *}
     */
    public Entity(String D, Scanner scan)
    {
        while(scan.hasNext())
        {
        	// End of entity
            if(scan.hasNext("}"))
            {
                // No more to add into this scanner
                break;
            }

            // Property Key Name
			String key = scan.next();
			
            // Start of property
            scan.next(); // {
            
            if(key.equalsIgnoreCase("Image")){
                saveImage(scan.next());
            }
            else if(key.equalsIgnoreCase("ID")){
                setID(scan.next());
            }
            else if(key.equalsIgnoreCase("MapPoint")){
                setPosition(new MapPoint(scan.nextDouble(),scan.nextDouble(),scan.nextDouble()));
            }
            else{
                putProperty(key,scan.next());
            }

            scan.next(); // }
        }
    }

	/**
     * Makes a clone of the given entityType
     * If a placedMapPoint is not supplied, then the entity will be given the same position as the supplied entityType
     */
    public Entity(Entity entityType, MapPoint... placedMapPoint){
        // Copy the instructions of the given entityType
        saveImage(entityType.getDirectory());
        properties.putAll(entityType.properties);

        size = new Dimension( drawImage.getIconWidth(),
            drawImage.getIconHeight() );

        if( placedMapPoint.length > 0 )
            position = new MapPoint(placedMapPoint[0]);
        else
            position = new MapPoint(entityType.getPosition());
    }
    
    /**
     * Draws the image of the entity
     */
    public void draw(Graphics g, HotelMap map){
    	draw(g);
    };
    
    /**
     * Draws the image of the entity
     */
    public void draw(Graphics g){
    	g.drawImage(drawImage.getImage(),
    			position.getX(),
    			position.getY(),
    			size.width,
    			size.height, null);
    };

    /**
     * Assigns a new ImageIcon to the direct image in the directory and sets up the dimensions of the icon.
     */
    public void saveImage(String directory){
        //drawImage_directory = directory;
        entityFile = new File(directory);
        drawImage = new ImageIcon(directory);
        size = new Dimension( drawImage.getIconWidth(),
        drawImage.getIconHeight() );        
    }

    /**
     * Returns an ArrayList of Strings that contain which should be saved from this class and it's super classes
     */
    public ArrayList<String> getSaveInfo()
    {
        ArrayList<String> saveInfo = new ArrayList<String>();
        saveInfo.addAll(super.getSaveInfo());

        saveInfo.add("SaveCharacter");
        saveInfo.add("{");
        saveInfo.add("\t" + getProperty("SaveCharacter").charAt(0));
        saveInfo.add("}");

        saveInfo.add("Image");
        saveInfo.add("{");
        saveInfo.add("\t" + drawImage.getDescription());
        saveInfo.add("}");

        for(String key : properties.keySet()){
            saveInfo.add(key);
            saveInfo.add("{");
            saveInfo.add("\t" + properties.get(key));
            saveInfo.add("}");
        }

        return saveInfo;
    }

    public boolean on(double x, double y, double z){ return ( x >= position.X() && x <= (position.X()+size.width) && 
            y >= position.Y() && y <= (position.Y()+size.height) );
    }
    
    public boolean on(MapPoint point){ return ( point.X() >= position.X() && point.X() <= (position.X()+size.width) && 
    		point.Y() >= position.Y() && point.Y() <= (position.Y()+size.height) );
    }

    public int width(){ return size.width; }
    public int height(){ return size.height; }

    public String getDirectory(){ return entityFile.getAbsolutePath(); }

    /**
     * Unique Properties of the Entity that are defined in entityList.txt or when loading a saved Map
     */
    public String getProperty(String key){ return properties.get(key); }

    public Map<String,String> getProperties(){ return properties; }

    public boolean hasProperty(String key){ return properties.containsKey(key); }

    public void putProperty(String key, String value){ properties.put(key,value); }

    public Rectangle2D getBounds(){ return new Rectangle2D.Double(position.X(),position.Y(),size.width, size.height); }

	public void drawSelection(Graphics g) {
		MapPoint pos = new MapPoint(position).translate(-2, -2, 0);
		Image img = new ImageIcon("resources/Entities/sprites/selection.png").getImage();
		g.drawImage(img,pos.getX(),pos.getY(),size.width+4,size.height+4,null);
	}
}
