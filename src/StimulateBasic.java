import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class StimulateBasic {

	public static final int LANE_LENGTH = 2;
	public static final int NUM_CARS = 100;
	public static final int SLOW_VELOCITY = 4;
	public static final int FAST_VELOCITY = 7;
	public static final int SAFE_DISTANCE = 0;
	public static ArrayList<Car> cars = new ArrayList<Car>();
	static CarModel model;
	public static void main(String[] args) {
		model = new CarModel(LANE_LENGTH);
		doFrame();
	}
	public static void doFrame(){
		Scanner s = new Scanner(System.in);
		int counter = 0;
		insertCar(0, 10, 3);
		insertCar(0, 9, 4);
		insertCar(0, 8, 5);
		insertCar(0, 6, 5);
		while(true){
			//if((cars.size() <= NUM_CARS) && counter % 1 == 0) insertCar();
			moveCars();
			printCars();
//			System.out.print(printCars());
			s.nextLine();
			counter++;
		}
//		for(int i = 0; i < 1000; i++){
//			System.out.print(i+1+""+printCars());
//			moveCars();
//		}
	}
	public static void insertCar(int lane, int pos, int speed){
		Car newCar = new Car(speed);
		newCar.position = pos;
		newCar.lane = lane;
		cars.add(newCar);
		model.insertCar(newCar);
	}
	public static void insertCar(){
		Random rand = new Random();
		Car newCar = new Car(SLOW_VELOCITY + rand.nextInt(2) * (FAST_VELOCITY - SLOW_VELOCITY));
		cars.add(newCar);
		model.insertCar(newCar);
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
						for(int k = 0; k < j; k++){
							carAhead = model.lanes.get(i).get(j - k - 1);
							if(carAhead.futureLane == carAhead.lane)
								break;
						}
						
						int gap = carAhead.position - currentCar.position - 1;
						int futureDistance = gap + carAhead.myVelocity - currentCar.myVelocity;
						if (futureDistance >= SAFE_DISTANCE){  //ample space
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
							
							int futureDistanceBehindLeftLane = Integer.MAX_VALUE;
							if(carBehindLeftLane!=null){
								futureDistanceBehindLeftLane = carBehindLeftLane.position 
										- currentCar.position + 1
										+ carBehindLeftLane.myVelocity 
										- currentCar.myVelocity;
							}
							
							int futureDistanceAheadLeftLane = Integer.MAX_VALUE;
							if(carAheadLeftLane!=null){
								futureDistanceAheadLeftLane = carAheadLeftLane.position
										- currentCar.position -1 
										+ carAheadLeftLane.myVelocity
										- currentCar.myVelocity;
							}
							
							if(carBehindLeftLane != null &&   // cannot pass due to car behind in last lane
								futureDistanceBehindLeftLane > -SAFE_DISTANCE){
									currentCar.deaccelerate(futureDistance - SAFE_DISTANCE);
							}else if(carAheadLeftLane == null ||   // can switch to left lane at full speed
									futureDistanceAheadLeftLane >= SAFE_DISTANCE){
										currentCar.switchLeft();
							}else if(futureDistanceAheadLeftLane >= futureDistance){
								currentCar.deaccelerateLeft(carAheadLeftLane.position 
										- currentCar.position + 1
										+ carAheadLeftLane.myVelocity - SAFE_DISTANCE);
							}else{
								currentCar.deaccelerate(futureDistance - SAFE_DISTANCE);
							}
						}
					}
				}
				else if (i==1){  //left lane
					Car carAheadRightLane = null;
					Car carBehindRightLane = null;
					for(Car c: model.lanes.get(i - 1)){
						if(c.position >= currentCar.position){
							carAheadRightLane = c;
						}
					}
					for(Car c: model.lanes.get(i - 1)){
						if(c.position <= currentCar.position){
							carBehindRightLane = c;
						}
						break;
					}
					
					int futureDistanceAheadRightLane = Integer.MAX_VALUE;
					if(carAheadRightLane!=null){
						 futureDistanceAheadRightLane = carAheadRightLane.position
								 				   - currentCar.position -1
								 				   + carAheadRightLane.myVelocity
								 				   - currentCar.myVelocity;
					}
					int futureDistanceBehindRightLane = Integer.MAX_VALUE;
					if(carBehindRightLane!=null){
						futureDistanceBehindRightLane = currentCar.position 
												   - carBehindRightLane.position - 1
								 				   + currentCar.myVelocity
								 				   - carBehindRightLane.myVelocity;
					}
					if(carAheadRightLane == null || futureDistanceAheadRightLane >= SAFE_DISTANCE){  //check to see if can join right
						if(carBehindRightLane == null || futureDistanceBehindRightLane >= SAFE_DISTANCE){
							currentCar.switchRight();
						}
					}else{
						if(j>0){
							Car carAhead = model.lanes.get(i).get(j-1);
							int gap = carAhead.position - currentCar.position - 1;
							int futureDistance = gap + carAhead.myVelocity - currentCar.myVelocity;
							
							if(futureDistance>=SAFE_DISTANCE){
								currentCar.regularMove();
							}
							else{
								currentCar.deaccelerate(futureDistance-SAFE_DISTANCE);
							}
						}
						else{
							currentCar.regularMove();
						}
					}
				}
			}
		}
		updatePosition();
	}
	
	public static void updatePosition(){
		for(int i = 0; i < model.lanes.size(); i++){
			for(int j = 0; j < model.lanes.get(i).size(); j++){
				Car currentCar = model.lanes.get(i).get(j);
				currentCar.position = currentCar.futurePosition;
				currentCar.lane = currentCar.futureLane;
			}
			model.lanes.get(i).clear();
		}
		
		for(Car car : cars){
			int newLanes = car.lane;
			model.lanes.get(newLanes).add(car);
		}
		
		for(int i = 0; i < model.lanes.size(); i++){
			ArrayList<Car> eachLane = model.lanes.get(i);
			Collections.sort(eachLane);
		}
	}
	
	public static void printCarsGraphical(){
		int maxPosition = 0; 
		for (Car car : cars){
			maxPosition = Math.max(maxPosition , car.position);
		}

		Car[][] gridForPrint = new Car[model.lanes.size()][maxPosition+1]; 
		
		String lane = "";
		for(int i = 0; i < model.lanes.size(); i++){
				for(int j = 0; j < model.lanes.get(i).size(); j++){
					Car currentCar = model.lanes.get(i).get(j);
					gridForPrint[i][currentCar.position] = currentCar;
				}
		}
		
		for(int i= 0; i < model.lanes.size(); i++){
			String thisLane = "";
			for(int j = 0; j < maxPosition; j++){
				if(gridForPrint[i][j] != null){
					thisLane += gridForPrint[i][j];
				}
				else{
					thisLane += "0";
				}
			}
			System.out.print(thisLane+"\n");
		}
		System.out.println(lane);
	}
	public static void printCars(){
		for(ArrayList<Car> i: model.lanes){
			for(Car j: i){
				System.out.print(j);
			}
			System.out.println();
		}
	}
	
	
}
