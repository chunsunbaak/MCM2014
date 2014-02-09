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
		futurePosition = position + myVelocity;
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
		futurePosition = position + myVelocity + newV;
		
	}

	public void deaccelerateLeft(int newV) {
		futurePosition = position + myVelocity + newV;
		futureLane++;
	}
	
	public int compareTo(Car other){
		return -(this.position - other.position);
	}
}
