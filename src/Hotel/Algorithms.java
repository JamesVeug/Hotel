package Hotel;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import Navigation.NavLink;
import Navigation.NavPoint;

/**
 * Algorithms designed to gather data in the fastest way possible.
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class Algorithms {

	/**
	 * An A* algorithm of which iterates through the out neighbours for the given startNavPoint and moves towards the end NavPoint until found.
	 * If the algorithm does not find the end NavPoint, it returns null.
	 * @param startNavPoint
	 * @param endNavPoint
	 * @return A Linked list of the NavPoints from the start point to the end point
	 */
	public static LinkedList<NavPoint> A_Star(NavPoint startPoint, NavPoint endPoint) {
		if (startPoint == null || endPoint == null)
			throw new NullPointerException(
					(startPoint == null ? "startPoint can NOT be null " : "")
							+ (endPoint == null ? "endPoint can NOT be null "
									: " "));

		// Make sure we can leave this navPoint at least
		if( startPoint.getConnections().isEmpty() ){
			System.out.println("A_STAR: No connections! " + startPoint + " to " + endPoint);
			return null;
		}

		HashSet<NavPoint> visited = new HashSet<NavPoint>();

		// Create the fringe and the first start
		PriorityQueue<AStar_Node> fringe = new PriorityQueue<AStar_Node>();
		fringe.add(new AStar_Node(null, startPoint,
				0, estimate(startPoint, endPoint),null));

		AStar_Node workingNavPoint = null;
		NavPoint nav = null;

		while (!fringe.isEmpty()) {
			// Take the highest priority NavPoint off the fringe
			workingNavPoint = fringe.poll();
			nav = workingNavPoint.getNavPoint();

			// Not Visited
			if ( !visited.contains(nav) ) {
				if ( nav == endPoint ) {

					// Found the NavPoint we want to work with
					break;
				} else {

					// Not our target NavPoint
					// Have not visited this NavPoint until now
					visited.add(nav);

					// Iterate through all of our NavLinks
					for (NavLink path : nav.links()) {
						NavPoint neighbour = path.getEnd();

						// Add the neighbour to our fringe if it's not visited yet
						// Length = NavPoint + this length
						if (!visited.contains(neighbour)) {
							double heuristic = estimate(neighbour, endPoint);
							double totalCost = (workingNavPoint.getLength()
									+ path.length());
							fringe.add(new AStar_Node(workingNavPoint, neighbour,
									totalCost, heuristic,path));
						}
					}
				}
			}
		}

		// We didn't find the end NavPoint so return null
		if (nav != endPoint) {
			return null;
		}

		// Return what we have worked with last
		// Even if it isn't what we wanted to find
		return BackTrack(workingNavPoint);
	}

	/**
	 * Performs a backtrack from the end NavPoint back to the start NavPoint saving each
	 * NavPoint visited into an Array in ordered position from start to finish.
	 * @param endNavPoint The NavPoint to backtrack from. Assumes there is at least one
	 * @return An array consisting of the path from the start NavPoint to the end NavPoint in order
	 */
	public static LinkedList<NavPoint> BackTrack(AStar_Node endNavPoint) {
		if (endNavPoint == null)
			return null;

		// Array in ordered position from start to end
		LinkedList<NavPoint> path = new LinkedList<NavPoint>();
		path.add(endNavPoint.getNavPoint());

		// Saves each NavPoint into the array from start to end
		while (endNavPoint != null) {
			// Depth is listed from 0 to end NavPoint
			path.add(0,endNavPoint.getNavPoint());
			endNavPoint = endNavPoint.getParent();

		}

		// Returns the ordered list
		return path;
	}

	/**
	 * Returns the estimate of the start and end NavPoints provided
	 *
	 * @param start
	 * @param end
	 * @return Distance to end from start
	 */
	private static double estimate(NavPoint start, NavPoint end) {
		return Math.abs(start.distance(end));
	}
}