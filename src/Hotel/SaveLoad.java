package Hotel;
import java.io.*;
import java.util.*;

import AI.Guest;
import AI.Staff;
import Objects.Entity;

public class SaveLoad
{
    public SaveLoad(){}

    /**
     * Loads a selected file
     */
    public static HotelMap load(File loadFile)
    {
        if( loadFile == null ) return null;
        
        HotelMap newMap;

        String description = "";
        try{
            System.out.println("Load in progress... ");

            Scanner scan = new Scanner(loadFile);

            if( !scan.hasNextInt() ){ scan.close(); throw new RuntimeException("Missing map length in file"); }
            int length = scan.nextInt();
            
            if( !scan.hasNextInt() ){ scan.close(); throw new RuntimeException("Missing map height in file"); }
            int height = scan.nextInt();
            
            newMap = new HotelMap(length, height);
            
                while(scan.hasNext())
                {
                	// Entity / AI / Tile
                    description = scan.next();

                    scan.next(); // {

                    if(description.equals("Receptionist")){ 
                        //newMap.addStaff(new Receptionist(description,scan));
                    }
                    else if(description.equals("Tile")){
                        //newMap.addTile(new Tile(description,scan));
                    }
                    else{
                       // newMap.addEntity(new Entity(description,scan));                      
                    }
                }
                scan.close();
        }catch(IOException e)
        {
            System.out.println(e); 
            return null;
        }

        System.out.println("Load complete: " + loadFile.getName());
        System.out.println("Tiles: " + newMap.getTilesSize());
        System.out.println("Entities: " + newMap.getEntities().size());
        System.out.println("Staff: " + newMap.getStaff().size());
        System.out.println("Guests: " + newMap.getGuests().size());
        return newMap;
    }

    /**
     * Saves the file
     */
    public static boolean save(File saveFile, HotelMap map)
    {
        try{

            System.out.println("Saving in progress... ");

            PrintStream out = new PrintStream(saveFile);

            for(Entity e : map.getEntities())
            {
                // Retrieves all the data from the line as a string
                out.println(e.getClass().getSimpleName()); // Entity / AI
                out.println("{"); // {
                ArrayList<String> saveInfo = e.getSaveInfo();
                for(String s : saveInfo)
                {
                    out.println("\t" + s); // Indent each of the bits of information to clean it clean
                }
                out.println("}"); // }
                out.println();
            }
            for(Guest e : map.getGuests())
            {
                // Retrieves all the data from the line as a string
                out.println(e.getClass().getSimpleName()); // Entity / AI
                out.println("{"); // {
                ArrayList<String> saveInfo = e.getSaveInfo();
                for(String s : saveInfo)
                {
                    out.println("\t" + s); // Indent each of the bits of information to clean it clean
                }
                out.println("}"); // }
                out.println();
            }
            for(Staff e : map.getStaff())
            {
                // Retrieves all the data from the line as a string
                out.println(e.getClass().getSimpleName()); // Entity / AI
                out.println("{"); // {
                ArrayList<String> saveInfo = e.getSaveInfo();
                for(String s : saveInfo)
                {
                    out.println("\t" + s); // Indent each of the bits of information to clean it clean
                }
                out.println("}"); // }
                out.println();
            }
            for(Tile[] t : map.getTiles())
            {
            	for(Tile e : t)
            	{
	                // Retrieves all the data from the line as a string
	                out.println("Tile"); // Entity / AI
	                out.println("{"); // {
	                ArrayList<String> saveInfo = e.getSaveInfo();
	                for(String s : saveInfo)
	                {
	                    out.println("\t" + s); // Indent each of the bits of information to clean it clean
	                }
	                out.println("}"); // }
	                out.println();
	            }
	        }

            out.close();

        }catch(Throwable e)
        {
            System.out.println(e); 
            return false;
        }

        System.out.println("Save Complete: " + saveFile.getName());
        return true;
    }
}
