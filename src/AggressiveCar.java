public class AggressiveCar implements Comparable<AggressiveCar>{
	double aggression;
	public int lastVelocity;
	public int myVelocity;
	public int lane;
	public int position;
	public int futurePosition, futureLane;
	public final int MAX_LENGTH = 500;
	public AggressiveCar(int velocity, double aggression){
		myVelocity = velocity;
		lane = 0;
		position = 0;
		this.aggression = aggression;
	}
	
	public String toString(){
		return "(" + lane + ", " + position + ", " + myVelocity + ", " + (aggression + "    ").substring(0, 4).trim() + ")";
		//return myVelocity+"";
	}
	public int getVelocity(){
		return (int) (myVelocity * ((aggression + 0.5)));
	}
	public void regularMove(){
		////////System.out.println("= = REGULARMOVE = =");
		futurePosition = (position + getVelocity()) % MAX_LENGTH;
		lastVelocity = getVelocity();
		//System.out.println("--- " + lastVelocity);
	}
	public void switchLeft(){
		////////System.out.println("= = SWITCHLEFT = =");
		futureLane = 1;
		regularMove();
	}
	public void switchRight(){
		////System.out.println("SWITCHED RIGHT");
		futureLane = 0;
		regularMove();
	}
//	public void deaccelerate(int newV) {
//		futurePosition = (position + getVelocity() + newV) % MAX_LENGTH;
//		lastVelocity = getVelocity() + newV;
//	}
	public void deaccelerateToSafeSpeed(AggressiveCar cur, AggressiveCar front){
		int gap = front.position - cur.position - 1;  //get distance between the two cars
		//System.out.println(gap);
		////////System.out.println(front.position);
		////////System.out.println(cur.position);
		futurePosition = (position + gap / 2) % MAX_LENGTH;
		lastVelocity = gap / 2;
		//System.out.println(cur);
		//System.out.println(front);
		//System.out.println("-- " + lastVelocity);
		////////System.out.println("= = DEACCELERATE = =");
		////////System.out.println("Deaccelerating to " + (gap / 2) + "...");
	}
	public void deaccelerateToSafeSpeedLeft(AggressiveCar cur, AggressiveCar front) {
//		futurePosition = (position + getVelocity() + newV) % 1000;
		int gap = front.position - cur.position - 1;
		futurePosition = (position + gap / 2) % MAX_LENGTH;
		lastVelocity = gap / 2;
		futureLane = 1;
		//System.out.println("- " + lastVelocity);
		////////System.out.println("= = DEACCELERATE LEFT = =");
	}
	public void deaccelerateToSafeSpeedRight(AggressiveCar cur, AggressiveCar front) {
//		futurePosition = (position + myVelocity + newV) % 1000;
		int gap = front.position - cur.position - 1;
		futurePosition = (position + gap / 2) % MAX_LENGTH;
		futureLane = 0;
		lastVelocity = gap / 2;
		//System.out.println("- " + lastVelocity);
		////System.out.println("= = DEACCELERATE LEFT = =");
	}
//	public void deaccelerateLeft(int newV) {
//		futurePosition = (position + getVelocity() + newV) % MAX_LENGTH;
//		futureLane++;
//	}
	
//	public void changeVelocity(int newV) {
//		myVelocity = newV;
//		regularMove();
//	}
//	
//	public void switchLeftAndChangeVelocity(int futureVelocity){
//		changeVelocity(futureVelocity);
//		lane++;
//	}
//	public void switchRightAndChangeVelocity(int futureVelocity){
//		changeVelocity(futureVelocity);
//		lane--;
//	}
	
	public int compareTo(AggressiveCar other){
		if(other.position == this.position)
			return other.lane - this.lane;
		return other.position - this.position;
	}
}