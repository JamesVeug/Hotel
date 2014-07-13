package Navigation;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import Hotel.HotelMap;
import Hotel.Point3D;


public class Navigation2 {

	NavPoint[][] graph;
	private int columns = 0;
	private int rows = 0;

	Set<NavPoint> points = new HashSet<NavPoint>();
	Set<NavLink> links = new HashSet<NavLink>();

	public Navigation2(int cols, int rows){
		this.graph = new NavPoint[cols][rows];
		this.columns = cols;
		this.rows = rows;
	}

	/**
	 * Navigations points to add to the graph.
	 * @param coord
	 */
	public void addNavigationPoint(Point3D coord){
		NavPoint nav = new NavPoint(coord);

		int x = (int)(coord.getX()/25);
		int y = (int)(coord.getY()/25);
		/*if( graph[x][y] != null ){
			// Get rid of the old navigation
			NavPoint old = graph[x][y];
			old.clearLinks();
		}*/

		// Add the node to the graph
		graph[x][y] = nav;
		points.add(graph[x][y]);
	}

	public void buildNodeGraph(){

		for( int x = 0; x < columns; x++ ){
			for( int y = 0; y < rows; y++ ){
				if( graph[x][y] != null ){
					assignNeighbours(graph[x][y]);
				}
			}
		}

	}

	/**
	 * Links all the neighbours to the given nav and the nav to all the neighbours on the graph
	 * @param nav
	 */
	public void assignNeighbours(NavPoint nav) {
		// Get all direct paths
		for(int y = -1; y < 2; y++){
			for(int x = -1; x < 2; x++){
				int tX = (int)(nav.getX()/25);
				int tY = (int)(nav.getY()/25);

				// Valid Square
				if( validSquare(tX+x,tY+y) && graph[tX+x][tY+y] != nav){

					double changeInZ = graph[tX+x][tY+y].Z() - nav.Z();

					// Corner paths are restricted
					if( Math.abs(x+y) != 1){
						if( !validSquare(tX+x,tY) || !validSquare(tX,tY+y) ){ continue; }
						if( graph[tX+x][tY].getZ() != nav.getZ()){ continue; }
						if( graph[tX][tY+y].getZ() != nav.getZ()){ continue; }
						if( changeInZ != 0 ){ continue; }
					}
					else{
						// Direct Paths have no restrictions
						if( changeInZ == Math.abs(0.5) ){ }
						else if( changeInZ != 0 ){ continue; }
					}

					NavLink link = nav.linkTo(graph[tX+x][tY+y]);
					links.add(link);
				}
			}
		}

	}

	public NavPoint getClosestNode(double x, double y){
		x = (int)Math.round(x/25);
		y = (int)Math.round(y/25);

		if( validSquare((int)x,(int)y) ){
			return graph[(int)x][(int)y];
		}

		return null;
	}

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
