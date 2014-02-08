public class Car {
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
		return Integer.toString(myVelocity);
	}
	public void regularMove(){
		futurePosition = position + myVelocity;
	}
	public void switchLeft(){
		lane++;
		regularMove();
	}

	public void deaccelerate(int newV) {
		futurePosition = position + newV;
		
	}

	public void deaccelerateLeft(int newV) {
		futurePosition = position + newV;
		lane++;
	}
}
