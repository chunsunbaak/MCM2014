public class Car implements Comparable<Car>{
	int myVelocity;
	int lane;
	int position;
	int futureLane;
	int futurePosition;
	public Car(int velocity){
		myVelocity = velocity;
		lane = 0;
		position = 0;
	}

	public String toString(){
		//return "(" + lane + ", " + myVelocity + ")";
		return myVelocity+"";
	}
	public void regularMove(){
		futurePosition = (position + myVelocity) % 1000;
	}
	public void switchLeft(){
		futureLane++;
		regularMove();
	}
	public void switchRight(){
		futureLane--;
		regularMove();
	}
	public void deaccelerate(int newV) {
		futurePosition = (position + myVelocity + newV) % 1000;
		
	}

	public void deaccelerateLeft(int newV) {
		futurePosition = (position + myVelocity + newV) % 1000;
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
		return other.position - this.position;
	}
}