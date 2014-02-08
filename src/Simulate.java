import java.util.ArrayList;
import java.util.Random;

public class Simulate {

	public static final int LANE_LENGTH = 100;
	public static final int NUM_CARS = 10;
	public static final int SLOW_VELOCITY = 5;
	public static final int FAST_VELOCITY = 8;
	public static final int SAFE_DISTANCE = 2;
	public static ArrayList<Car> cars = new ArrayList<Car>();
	static CarModel model;
	public static void main(String[] args) {
		model = new CarModel(LANE_LENGTH);
		while(true){
			doFrame();
		}
		
	}
	public static void doFrame(){
		if(cars.size() <= NUM_CARS){
			insertCar();
		}
		moveCars();
	}
	public static void insertCar(){
		Random rand = new Random();
		Car newCar = new Car(SLOW_VELOCITY + rand.nextInt(2) * (FAST_VELOCITY - SLOW_VELOCITY));
		model.insertCar(newCar);
		cars.add(newCar);
	}
	static void moveCars(){
		for(int i = 0; i < model.lanes.size(); i++){
			for(int j = 0; j < model.lanes.get(i).size(); j++){
				Car currentCar = model.lanes.get(i).get(j);
				if(i == 0){  //right lane
					if(j == 0){  // first car
						currentCar.regularMove();
					}else{
						Car carAhead = model.lanes.get(i).get(j - 1);
						int gap = carAhead.position - currentCar.position - 1;
						int futureDistance = gap + carAhead.myVelocity - currentCar.myVelocity;
						if(futureDistance >= SAFE_DISTANCE){  //ample space
							currentCar.regularMove();
						}else{  //not enough space, try to pass
							Car carAheadLeftLane = null;
							Car carBehindLeftLane = null;
							for(Car c: model.lanes.get(i + 1)){
								if(c.position >= currentCar.position){
									carAheadLeftLane = c;
								}
							}
							for(Car c: model.lanes.get(i + 1)){
								if(c.position < currentCar.position){
									carBehindLeftLane = c;
									break;
								}
							}
							if(carBehindLeftLane != null &&   // cannot pass due to car behind in last lane
									carBehindLeftLane.position 
									- currentCar.position - 1
									+ carBehindLeftLane.myVelocity 
									- currentCar.myVelocity > -SAFE_DISTANCE){
										currentCar.deaccelerate(futureDistance - SAFE_DISTANCE);
							}else if(carAheadLeftLane == null ||   // can switch to left lane at full speed
									carAheadLeftLane.position 
									- currentCar.position - 1
									+ carAheadLeftLane.myVelocity 
									- currentCar.myVelocity >= SAFE_DISTANCE){
								currentCar.switchLeft();
							}else if(futureDistance//)
								currentCar.deaccelerateLeft(carAheadLeftLane.position 
										- currentCar.position - 1
										+ carAheadLeftLane.myVelocity - SAFE_DISTANCE);
							}
						}
					}
				}
			}
		}
	}
}
