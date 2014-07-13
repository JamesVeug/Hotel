package Navigation;
import java.awt.Graphics;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import Hotel.HotelMap;
import Hotel.Point3D;


public class Navigation {

	private NavPoint[][] graph;
	private int columns = 0;
	private int rows = 0;

	private Set<NavPoint> points = new HashSet<NavPoint>();
	private Set<NavLink> links = new HashSet<NavLink>();

	public Navigation(int cols, int rows){
		this.graph = new NavPoint[cols][rows];
		this.columns = cols;
		this.rows = rows;
	}

	/**
	 * Adds the given coordinate to the Navigation graph graph
	 * @param coord
	 */
	public void addNavigationPoint(Point3D coord){
		NavPoint nav = new NavPoint(points.size(), coord);

		int x = (int)(coord.getX()/25);
		int y = (int)(coord.getY()/25);

		// Add the node to the graph
		graph[x][y] = nav;
		points.add(graph[x][y]);
	}

	/**
	 * Adds Connects all the navigation points together to form pathing.
	 */
	public void buildNodeGraph(HotelMap map){
		long totalTime = System.currentTimeMillis();
		long time = System.currentTimeMillis();

		//int numProcessors = Runtime.getRuntime().availableProcessors();
		System.out.print("Resetting NodeGraph... ");
		points.clear();
		links.clear();
		for( int x = 0; x < columns; x++ ){
			for( int y = 0; y < rows; y++ ){
				graph[x][y] = null;
			}
		}
		System.out.printf("%dms\n",System.currentTimeMillis()-time);
		time = System.currentTimeMillis();

		System.out.print("Building NodeGraph... ");
		for( int x = 0; x < columns; x++ ){
			for( int y = 0; y < rows; y++ ){
				if( map.getTiles()[x][y] != null ){
					addNavigationPoint(map.getTiles()[x][y].getPosition());
				}
			}
		}
		System.out.printf("%dms\n",System.currentTimeMillis()-time);
		time = System.currentTimeMillis();


		System.out.print("Linking NodeGraph... ");
		for( int x = 0; x < columns; x++ ){
			for( int y = 0; y < rows; y++ ){
				if( graph[x][y] != null ){
					assignNeighbours(graph[x][y]);
				}
			}
		}
		System.out.printf("%dms\n",System.currentTimeMillis()-time);
		time = System.currentTimeMillis();


		System.out.print("Pruning... ");
		for(NavPoint nav : points){
			if( nav.links().size() == 2 ){

			}
		}

		System.out.printf("%dms\n",System.currentTimeMillis()-time);
		time = System.currentTimeMillis();

		//time = System.currentTimeMillis() - time;
		System.out.printf("Nodegraph built in %dms\n",System.currentTimeMillis()-totalTime);
	}

	/**
	 * Links all the neighbours to the given nav and the nav to all the neighbours on the graph
	 * @param nav
	 */
	public void assignNeighbours(NavPoint nav) {
		//  x + y Matrix
		// -2 -1  0
		// -1  0  1
		//  0  1  2

		// Graph point of the navPoint
		int tX = (int)(nav.getX()/25);
		int tY = (int)(nav.getY()/25);

		// Y == offset from the nav points location
		for(int y = -1; y < 2; y++){

			// X == offset from the nav points location
			for(int x = -1; x < 2; x++){


				if( !validSquare(tX+x,tY+y) || (x == 0 && y == x) ){
					continue;
				}

				// Change in Z between the graph point and the Navpoint
				double changeInZ = graph[tX+x][tY+y].Z() - nav.Z();

				if( Math.abs(x+y) != 1){
					// Direct paths adjacent to this corner need to be valid too
					if( !validSquare(tX+x,tY) || !validSquare(tX,tY+y) ){ continue; }

					// Checks heights of directed paths as well
					if( graph[tX+x][tY].getZ() != nav.getZ()){ continue; }
					if( graph[tX][tY+y].getZ() != nav.getZ()){ continue; }

					// Should be on the same level
					if( changeInZ != 0 ){ continue; }
				}
				else{
					// Direct Paths are only restricted by heights
					if( changeInZ == Math.abs(0.5) ){ }
					else if( changeInZ != 0 ){ continue; }
				}

				// Link the navPoint to the graphNode
				links.add(nav.linkTo(graph[tX+x][tY+y]));
			}
		}
	}

	/**
	 * An extention of assignNeighbours, try and extends the navpoint out further
	 * @param start
	 * @param target
	 */
	public void assignNeighboursRec(NavPoint start, NavPoint target, Set<NavPoint> visited){

		// Check to make sure that we can move to this location
		if( canMoveTo(start, target) ){

			// Fringe for out neighbours
			ArrayDeque<NavPoint> fringe = new ArrayDeque<NavPoint>();

			// Create Link from nav point to the graph point
			if( !start.isLinkedTo(target) ){

				NavLink link = start.linkTo(target);
				link.setID(links.size());

				// Record link
				links.add(link);

				// Graph point of the navPoint
				int tX = (int)(target.getX()/25);
				int tY = (int)(target.getY()/25);

				// Y == offset from the nav points location
				for(int y = -1; y < 2; y++){

					// X == offset from the nav points location
					for(int x = -1; x < 2; x++){

						// Check we have a navpoint
						if( validSquare(tX+x,tY+y) && (x+y) != 0 ){

							// Make sure we haven't visited the point yet
							if( !visited.contains(graph[tX+x][tY+y])){
								fringe.add(graph[tX+x][tY+y]);
							}
						}
					}
				}

				/**
				 * Work with all the neighbours around this target node
				 */
				while(!fringe.isEmpty()){
					NavPoint pop = fringe.pop();

					// Make sure this node is not visited yet
					if( !visited.contains(pop) ){

						visited.add(pop);
						//assignNeighboursRec(start,pop,visited);
					}
				}
			}
		}
	}

	/**
	 *
	 * @param start Original position
	 * @param x Offset in x by the nav points location
	 * @param y Offset in y by the nav points location
	 * @return True if we can move to the end location
	 */
	private boolean canMoveTo(NavPoint start, NavPoint target){

		// Graph point of the navPoint
		int tX = (int)(start.getX()/25);
		int tY = (int)(start.getY()/25);

		int x = (int)(target.getX()/25);
		int y = (int)(target.getY()/25);

		// Check for valid position, and it isn't the same position that we were originally given
		if( !validSquare(x,y) || !validSquare(tX,tY) || start == target){
			return false;
		}

		// Amount of steps alone the line
		int MaxSteps = Math.max(Math.abs(x), Math.abs(y));

		// Check if it's only 1 navpoint away
		//if(MaxSteps == 1){
			//System.out.println(tX + "," + tY + " 1 Step");
		//}

		// Step along the line from the navPoints location
		// All the way to the x,y position trying to find an invalid square
		for(int s = 1; s <= MaxSteps; s++){
			int sX = Math.round(s/(x != 0 ? x : s));
			int sY = Math.round(s/(y != 0 ? y : s));

			System.out.println(sX + " , " + sY);

			if( validSquare(x+sX, y+sY) ){

				// Point in between start and target node
				NavPoint travelNode = graph[x+sX][y+sY];

				// Change in Z between the graph point and the Navpoint
				double changeInZ = travelNode.Z() - start.Z();

				// Corner paths
				if( Math.abs(sX+sY) != 1){
					// Direct paths adjacent to this corner need to be valid too
					if( !validSquare(tX+sX,tY) || !validSquare(tX,tY+sY) ){ return false; }

					// Checks heights of directed paths as well
					if( graph[tX+sX][tY].getZ() != start.getZ()){ return false; }
					if( graph[tX][tY+sY].getZ() != start.getZ()){ return false; }

					// Should be on the same level
					if( changeInZ != 0 ){ return false; }
				}
				else{
					// Direct Paths are only restricted by heights
					if( changeInZ == Math.abs(0.5) ){ }
					else if( changeInZ != 0 ){ return false; }
				}
			}

		}
		return true;

	}



	/**
	 * Expands the primary NavPoint's neighbours
	 * @param primary
	 * @param neighbour
	 */
	public void expandNavigationRec(NavPoint primary, int tX, int tY, double tZ){
		// Get all direct paths
		for(int y = -1; y < 2; y++){
			for(int x = -1; x < 2; x++){
				if( x+y == 0) continue; // Already determined this point

				// Valid Square
				if( validSquare(tX+x,tY+y)){

					double changeInZ = graph[tX+x][tY+y].Z() - tZ;

					// Corner paths are restricted
					if( Math.abs(x+y) != 1){
						if( !validSquare(tX+x,tY) || !validSquare(tX,tY+y) ){ continue; }
						if( graph[tX+x][tY].getZ() != (int)tZ){ continue; }
						if( graph[tX][tY+y].getZ() != (int)tZ){ continue; }
						if( changeInZ != 0 ){ continue; }
					}
					else{
						// Direct Paths have no restrictions
						if( changeInZ == Math.abs(0.5) ){ }
						else if( changeInZ != 0 ){ continue; }
					}

					NavLink link = primary.linkTo(graph[tX+x][tY+y]);
					links.add(link);
				}
			}
		}
	}

	/**
	 * Rounds the coordinates and returns the point on the graph
	 * @param x
	 * @param y
	 * @return Navigation point at x, y or null if not a valid square.
	 */
	public NavPoint getClosestNode(double x, double y){

		// Round coordinates to fit the graph
		x = (int)Math.round(x/25);
		y = (int)Math.round(y/25);

		// Check if valid coordinates
		if( validSquare((int)x,(int)y) ){
			return graph[(int)x][(int)y];
		}

		// Not valid points
		return null;
	}

	/**
	 * Checks to make sure the given coordinates are on the graph and the point on the graph is not null
	 * @param x
	 * @param y
	 * @return Valid square is within bounds and not null
	 */
	private boolean validSquare(int x, int y){
		return x >= 0 && x < columns && y >= 0 && y < rows && graph[x][y] != null;
	}

	public void draw(Graphics g, HotelMap map){
		if( !map.shouldDraw("NAVIGATION") ) return;

		for( NavLink l : links){
			l.draw(g);
		}
		for( NavPoint p : points){
			p.draw(g);
		}
	}

	public Set<NavPoint> getPoints() {
		return points;
	}
}
