package Hotel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import AI.Guest;
import AI.Receptionist;
import AI.Staff;
import AI.Zombie;
import Navigation.Navigation;
import Objects.Bed;
import Objects.Entity;
import Objects.ObjectBase;

public class HotelMap {
	private HashMap<String,Boolean> shouldDraw = new HashMap<String,Boolean>(){
		private static final long serialVersionUID = -3193025359181146352L;

		{
			put("NAVIGATION",false);
			put("PATHING",false);
		}
	};


	private ArrayList<FloatingText> sprites = new ArrayList<FloatingText>();

	private ArrayList<Guest> guests = new ArrayList<Guest>();
	private ArrayList<Staff> staff = new ArrayList<Staff>();

	// For Testing
	public ArrayList<Zombie> zombies = new ArrayList<Zombie>();

	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Bed> rooms = new ArrayList<Bed>();
	private HashSet<Tile> exit = new HashSet<Tile>();

	private Entity selectedEntity = null;
	private Navigation navigation;

	private Money totalFunds = new Money(0);

	// List of Columns of Rows
	private Tile[][] tiles;
	private int tilesAdded = 0;
	private int columns = 0;
	private int rows = 0;

	public HotelMap(int column, int row) {
		tiles = new Tile[column][row];
		this.columns = column;
		this.rows = row;
	}

	/**
	 * Creates a basic map layout using walls as Black and floor as white for testing
	 * @param layout String of how much the map should be. Map should be squared
	 */
	public HotelMap(String[] layout) {
		this.columns = layout[0].length();
		this.rows = layout.length;

		tiles = new Tile[columns][rows];
		navigation = new Navigation(columns,rows);

		for( int y = 0; y < rows; y++){
			for( int x = 0; x < columns; x++){
				char c = layout[y].charAt(x);

				// Are we trying to create an entity?
				if( c == 'D' || c == 'Z' || c == 'B' || c == 'G' || c == 'R'){
					switch(c){
					// Save Entity
						case 'G': this.addGuest( new Guest(new MapPoint(x*25,y*25,0), "resources/Entities/object/AI/g.png")); break;
						case 'R': this.addStaff( new Receptionist(new MapPoint(x*25,y*25,0), "resources/Entities/object/AI/r.png")); break;
						case 'Z': zombies.add( new Zombie(new MapPoint(x*25,y*25,0), "resources/Entities/object/AI/z.png")); break;
						case 'B':
							// Beds
							String room = String.valueOf(0) + String.valueOf(rooms.size()+1);
							Bed bed = new Bed(room,new MapPoint(x*25,y*25,0));
							this.addEntity( bed );
							this.addRoom( bed );
							break;
					}
					// Make the floor underneath the entity white
					c = ' ';
				}
				tiles[x][y] = getLayoutTile(c,x*25,y*25);
			}
		}

		navigation.buildNodeGraph(this);
	}


	public Tile getLayoutTile(char c, int x, int y){
		Tile t = null;

		switch(c){
			case '#': t = new Tile(new MapPoint(x,y,1), "resources/Entities/tile/black.png"); break;
			case '|': t = new Tile(new MapPoint(x,y,1), "resources/Entities/tile/black.png"); break;
			case '_': t = new Tile(new MapPoint(x,y,1), "resources/Entities/tile/black.png"); break;
			case '>': t = new Tile(new MapPoint(x,y,0.5), "resources/Entities/tile/BlackfadeRight.png"); break;
			case ' ': t = new Tile(new MapPoint(x,y,0), "resources/Entities/tile/carpet.png"); break;
			case ',': t = new Tile(new MapPoint(x,y,0), "resources/Entities/tile/gray.png"); break;
			case 'x': t = new Tile(new MapPoint(x,y,0), "resources/Entities/tile/gray.png"); exit.add(t); break;
			default: return new Tile(new MapPoint(x,y,-1), "resources/Entities/tile/unknown.png");
		}

		return t;
	}

	public void addEntity(Entity newEntity) {
		entities.add(newEntity);
	}

	public void addRoom(Bed room) {
		rooms.add(room);
	}

	public Tile getTile(int tX, int tY){
		if( validSquare(tX,tY) ){
			return tiles[tX][tY];
		}

		// // Invalid Tile
		return null;
	}

	/**
	 * Checks to make sure the given coordinates are on the graph and the point on the graph is not null
	 * @param x
	 * @param y
	 * @return Valid square is within bounds and not null
	 */
	private boolean validSquare(int x, int y){
		return x >= 0 && x < columns && y >= 0 && y < rows;
	}

	/**
	 * Sets a new tile on the graph and returns the replaced tile
	 * @param tX
	 * @param tY
	 * @param newTile
	 * @return
	 */
	public Tile setTile(int tX, int tY, Tile newTile){
		Tile t = tiles[tX][tY];
		tiles[tX][tY] = newTile;

		return t;
	}

	public void addTile(Tile newTile) {
		int col = newTile.getPosition().getX()/25;
		int row = newTile.getPosition().getY()/25;

		// Increase the size of the map if we need to
		if( col >= tiles.length || row >= tiles[0].length)
			this.setDimensions(tiles[0].length*2, tiles.length*2);

		// Save a counter
		if( tiles[col][row] == null ){
			tilesAdded++;
		}

		tiles[col][row] = newTile;
		//linkTile(col, row, newTile);
	}

	public void linkTile(int col, int row, Tile t){
		for( int x = -1; x < 2; x++){
			for( int y = -1; y < 2; y++){
				if( x >= 0 && y >= 0 && !(x == 1 && y == 1) ){
					t.linkNeighbour(tiles[col+x][row+y]);
				}
			}
		}
	}

	public void addGuest(Guest newGuest) {
		guests.add(newGuest);
	}

	private void addStaff(Staff newStaff) {
		staff.add(newStaff);

	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	public ArrayList<Guest> getGuests() {
		return guests;
	}

	public ArrayList<Staff> getStaff() {
		return staff;
	}

	public void draw(Graphics g) {
		for (Tile[] t : tiles) {
			for (Tile tile : t) {
				if( tile != null )
					tile.draw(g, this);
			}
		}

		navigation.draw(g,this);

		if( selectedEntity != null ){
			selectedEntity.drawSelection(g);
		}

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).draw(g, this);
		}
		for (int i = 0; i < guests.size(); i++) {
			guests.get(i).draw(g, this);
		}
		for (int i = 0; i < staff.size(); i++) {
			staff.get(i).draw(g, this);
		}
		for (int i = 0; i < sprites.size(); i++) {
			sprites.get(i).draw(g);
		}
		for (int i = 0; i < zombies.size(); i++) {
			zombies.get(i).draw(g,this);
		}

		g.setColor(Color.GREEN);
		g.drawString("Funds: $" + totalFunds, 0, 10);
		
		g.setColor(Color.white);
		g.drawString("GuestCount: " + guests.size(), 0, 20);
	}

	public void setDimensions(int columns, int rows) {
		Tile[][] newTileSet = new Tile[columns][rows];

		// Copy over all the tiles
		if( tiles != null){
			for(int x = 0; x < tiles.length; x++)
				for(int y = 0; y < tiles[0].length; y++)
					newTileSet[x][y] = tiles[x][y];
		}
		tiles = newTileSet;
	}

	public void setDraw(String Command, boolean value){
		shouldDraw.put(Command,value);
	}

	public boolean shouldDraw(String command){
		return shouldDraw.containsKey(command) && shouldDraw.get(command);
	}

	public HashMap<String, Boolean> getShouldDraws(){
		return shouldDraw;
	}

	public Receptionist getReceptionist(){
		for(int i = 0; i < staff.size(); i++){
			Staff s = staff.get(i);
			if( s instanceof Receptionist){
				Receptionist r = (Receptionist)s;
				if( r.getGuest() == null || i == staff.size()-1 ){
					return r;
				}
			}
		}

		// Don't have a receptionist
		return null;
	}

	public Tile getExit(){
		final int chance = exit.size();

		// Random Exit?
		for(Tile t : exit){
			Random rand = new Random();
			int result = rand.nextInt(chance);

			if( result == chance-1){
				return t;
			}
		}

		// Not Random
		for(Tile t : exit)
			return t;

		// Never reached
		return null;
	}

	public boolean outsideHotel(MapPoint point){
		for(Tile t : exit){
			if( t.on(point) ){
				return true;
			}
		}

		return false;
	}

	public void sellRoom(Bed room, Guest guest){
		// Create floating sprite
		MapPoint pos = new MapPoint(guest.getPosition().X()+(guest.width()/2),guest.getPosition().Y()+(guest.height()/2),guest.getPosition().Z());
		FloatingText floater = new FloatingText("$"+room.cost, pos, Color.green, 3);
		sprites.add(floater);

		// Recort money
		totalFunds.value += room.cost;
	}

	public Entity getEntity(int x, int y){
		for(Entity e : entities){
			if( e.on(x,y,0) ){
				return e;
			}
		}

		return null;
	}

	public Guest getGuest(int x, int y){
		for(Guest g : guests){
			if( g.on(x,y,0) ){
				return g;
			}
		}

		return null;
	}

	public void cleanRoom(Bed bed) {
		if( bed.isDirty() && !bed.rented() ){
			bed.clean();
		}

	}

	public void addFloatingText(FloatingText f) {
		sprites.add(f);
	}
	
	public ArrayList<FloatingText> sprites(){
		return sprites;
	}

	public ObjectBase selectedObject() {
		return selectedEntity;
	}

	public void setSelectedObject(Entity selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	public int getTilesSize() {
		return tilesAdded;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public Navigation getNavigation() {
		return navigation;
	}

	public int getWidth() {
		return columns;
	}

	public int getHeight() {
		return rows;
	}
}
