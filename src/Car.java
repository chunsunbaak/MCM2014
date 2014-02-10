public class Car implements Comparable<Car>{
	int myVelocity;
	int lane;
	int position;
	int futureLane;
	int futurePosition;
	static int MAX_LENGTH = 100;
	static int counter = 0;
	int id;
	public Car(int velocity){
		myVelocity = velocity;
		lane = 0;
		position = 0;
		id = counter;
		counter++;
	}

	public String toString(){
		return "(" + lane + ", " + position + ", " + myVelocity + ")";
		//return myVelocity+"";
	}
	public void regularMove(){
		System.out.println("= = REGULARMOVE = =");
		futurePosition = (position + myVelocity) % MAX_LENGTH;
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
		futurePosition = (position + myVelocity + newV) % MAX_LENGTH;
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
//		futurePosition = (position + myVelocity + newV) % 1000;
		int gap = front.position - cur.position - 1;
		futurePosition = (position + gap / 2) % MAX_LENGTH;
		futureLane++;
		System.out.println("= = DEACCELERATE LEFT = =");
	}
	public void deaccelerateLeft(int newV) {
		futurePosition = (position + myVelocity + newV) % MAX_LENGTH;
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