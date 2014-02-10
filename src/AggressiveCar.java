public class AggressiveCar extends Car{
	double aggression;
	public AggressiveCar(int velocity, double aggression){
		super(velocity);
		this.aggression = aggression;
	}

	public String toString(){
		return "(" + lane + ", " + position + ", " + myVelocity + ", " + aggression + ")";
		//return myVelocity+"";
	}
	public int getVelocity(){
		return (int) (myVelocity * (1 + (aggression - 0.5)));
	}
	public void regularMove(){
		System.out.println("= = REGULARMOVE = =");
		futurePosition = (position + getVelocity()) % MAX_LENGTH;
	}
	public void switchLeft(){
		System.out.println("= = SWITCHLEFT = =");
		futureLane++;
		regularMove();
	}
	public void switchRight(){
		futureLane--;
		regularMove();
	}
	public void deaccelerate(int newV) {
		futurePosition = (position + getVelocity() + newV) % MAX_LENGTH;
	}
	public void deaccelerateToSafeSpeed(Car cur, Car front){
		int gap = front.position - cur.position - 1;  //get distance between the two cars
		System.out.println(front.position);
		System.out.println(cur.position);
		futurePosition = (position + gap / 2) % MAX_LENGTH;
		System.out.println("= = DEACCELERATE = =");
		System.out.println("Deaccelerating to " + (gap / 2) + "...");
	}
	public void deaccelerateToSafeSpeedLeft(Car cur, Car front) {
//		futurePosition = (position + getVelocity() + newV) % 1000;
		int gap = front.position - cur.position - 1;
		futurePosition = (position + gap / 2) % MAX_LENGTH;
		futureLane++;
		System.out.println("= = DEACCELERATE LEFT = =");
	}
	public void deaccelerateLeft(int newV) {
		futurePosition = (position + getVelocity() + newV) % MAX_LENGTH;
		futureLane++;
	}
	
	public void changeVelocity(int newV) {
		myVelocity = newV;
		regularMove();
	}
	
	public void switchLeftAndChangeVelocity(int futureVelocity){
		changeVelocity(futureVelocity);
		lane++;
	}
	public void switchRightAndChangeVelocity(int futureVelocity){
		changeVelocity(futureVelocity);
		lane--;
	}
	
	public int compareTo(Car other){
		if(other.position == this.position)
			return other.lane - this.lane;
		return other.position - this.position;
	}
}