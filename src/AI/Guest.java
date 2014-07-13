package AI;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;

import AI.Orders.*;
import Hotel.FloatingText;
import Hotel.FunnyNames;
import Hotel.HotelMap;
import Hotel.MapPoint;
import Hotel.Money;
import Hotel.Point3D;
import Navigation.NavPoint;
import Objects.Bed;
import Objects.Entity;
import Objects.ObjectBase;

/**
 * A finate state controlled AI that performs actions according to the bed, money and state that they are in.
 *
 * @author James Veugelaers
 * @version 15/15/2014
 */
public class Guest extends AI{

	private ArrayList<FloatingText> sprites = new ArrayList<FloatingText>();

	// Stack of orders performing to get the Guest to do things
	private Stack<Order> orders = new Stack<Order>();
	private Emotions emotions = new Emotions();

	private final String name;
	private Entity occupiedBy = null;
    private Money funds = new Money(1000);
    private Bed bed = null;

    /**
     * Constructor for objects of class AI
     */
    public Guest(MapPoint point, String image){
        setPosition(point);
        saveImage(image);
        faceImage = new ImageIcon(getDirectory());
        name = FunnyNames.getRandom();
        
        Random rand = new Random();
   	 	emotions.Sleepiness = rand.nextInt(10);
    }

    public void assignRandomEmotions(){
    	 emotions = new Emotions();

    	 Random random = new Random();
    	 emotions.Bladder = 5 + random.nextInt(5);
    	 emotions.Sleepiness = 5 + random.nextInt(5);
    	 emotions.Hunger = 5 + random.nextInt(5);
    	 emotions.Happiness = 5 + random.nextInt(5);
    }

    @Override
    public void think(HotelMap map){

    	if( !orders.isEmpty() ){
    		
    		// Execute Order
    		// Remove from stack if executed
        	Order order = orders.peek();
        	
        	//System.out.println(orders.peek());
        	
        	try{
        		boolean executed = order.execute(map, this);
        		if( executed ){
        			orders.pop();
        		}
        		
        	}catch(FailedOrderException e){
        		order.pop().fail(e);
        	}
        	
    		
    	}else{
    		//System.out.println("No Orders:");
    		if( emotions.Sleepiness >= 5 ){
    			this.addNewOrder(new GoToBed());
    		}
    		else{
    			// We want to leave the hotel
    			this.addNewOrder(new LeaveHotel());
    			
    			// First we will check out
    			this.addNewOrder(new CheckOut());
    		}
    	}
    }
       
    /**
     * Prints all the orders from this guest
     */
    public void printOrders(){
    	for(Order order : orders){
    		System.out.println("Stack : " + order.getClass().getSimpleName());
    	}
    }

    public void addNewOrder(Order newOrder){
		//System.out.println("Adding: " + newOrder.getClass().getSimpleName());
    	orders.add(newOrder);

//    	System.out.println("================");
//    	printOrders();
//    	System.out.println("================\n\n");
    }

	/**
     * Talk to another AI if we can
     * @param other
     */
    public boolean talkTo(AI other){
    	if( other instanceof Receptionist){

    		Receptionist r = (Receptionist)other;
    		if( r.canTalk(this) ){
    			r.talkTo(this);

    			// Talked to the ai
    			return true;
    		}
    	}

    	// Didn't talk to the AI
    	return false;
    }


    /**
     * Takes a since step towards the specific target position
     * @param targetPosition
     * @param map
     */
    public void stepTowardsPosition(Point3D targetPosition, HotelMap map){
    	//Point3D point = path.get(0);
    	//System.out.println("    Following Path" + this.getPosition() + " -> " + point);
    	double x = targetPosition.X()-this.getPosition().X();
    	double y = targetPosition.Y()-this.getPosition().Y();
    	double z = targetPosition.Z()-this.getPosition().Z();

    	if( position.distance(targetPosition) == 0 ){
    		// Too close to old position so get rid of it and find the next position
    		if( path.getFirst().Z() != position.Z() ){
    			// Z not determined in Point3D.distance
    			moveBy(0,0, (path.getFirst().Z() - position.Z()));
    		}

    		path.remove(0);
    	}
    	else{
    		// Move towards the next position
    		double nextX = Math.signum(x);
    		double nextY = Math.signum(y);
    		double nextZ = z/25;

    		// If we are sleepy, move slowly
    		if( state == States.SLEEPY ){
    			nextX /= 2;
    			nextY /= 2;
    			nextZ /= 2;
    		}

        	moveBy(nextX, nextY,nextZ);
    	}
    }

    @Override
    public void draw(Graphics g, HotelMap map)
    {
    	if( !shouldDraw || occupiedBy != null ){ return; }

    	// Draw pathing if we are allowed
    	if( map.shouldDraw("PATHING") && path != null && !path.isEmpty() ){
    		g.setColor(Color.blue);
    		g.drawLine((int)path.getFirst().getX()+10, (int)path.getFirst().getY()+10,
    				(int)getPosition().getX()+10, (int)getPosition().getY()+10 );

    		// Draw the path from this point to the target point
    		NavPoint b = null;
    		for( NavPoint a : path ){
    			if( b != null ){
    				g.drawLine(a.getX()+10, a.getY()+10,
 	    				   b.getX()+10, b.getY()+10 );
    			}
    			b = a;
    		}
    	}

    	// Draw image
        super.draw(g, map);

        // Draw Sprites
        for(FloatingText floater : sprites){
        	floater.draw(g);
        }
    }

    public void draw(Graphics g){

    	// Draw image
    	super.draw(g);

    	// Draw Sprites
        for(FloatingText floater : sprites){
        	floater.draw(g);
        }
    }

	public ObjectBase getTarget() {
		return target;
	}

	public void setTarget(ObjectBase target) {
		this.target = target;
	}

    public Bed getRoom(){
    	return bed;
    }
    public void setRoom(Bed bed){
    	this.bed = bed;
    }

    public Money funds(){
    	return funds;
    }

    public boolean canAfford(int cost){
    	return funds.value >= cost;
    }

    /**
     * Get the guest to spend the given amoutn of money
     * @param cost Money to spend
     */
	public void spend(int cost) {
		funds.value -= cost;

	}

	/**
	 * Assigns the Guest to be occupied by the specific entity
	 * @param object
	 */
	public void occupiedBy(Entity object) {
		this.occupiedBy = object;
	}

	public String name() {
		return name;
	}

	public Emotions getEmotions() {
		return emotions;
	}

	public boolean inBed() {
		return occupiedBy != null && occupiedBy instanceof Bed;
	}

	/**
	 * Vacates from the occupied entity if any
	 */
	public void vacate() {
		if( occupiedBy == null ) return;
		
		if( occupiedBy instanceof Bed ){
			((Bed)occupiedBy).vacate(this);
		}
		
	}

	public Order getCurrentOrder() {
		return orders.isEmpty() ? null : orders.peek();
	}
	
	public Order popCurrentyOrder(){
		return orders.isEmpty() ? null : orders.pop();
	}
}
