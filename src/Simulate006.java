import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Simulate006 {
	// 001 Basic Model
	// 002 Add Safe Distance
	// 003 Add Loop (applies to previous versions) No change in Simulate
	// 004 Aggressiveness - Uses AggressiveCar instead of AggressiveCar, only affects
	// 005 Aggressiveness - Uses Aggressiveness to determine rate of switching TODO: make it possible to have a car crash
	// 006 Aggressiveness - Uses Aggressiveness to determine safety distance (need to have car crashes)
	static ArrayList<ArrayList<AggressiveCar>> lanes;
	public static final int LANE_LENGTH = 2;
	public static final int NUM_CARS = 100;
	public static final int SLOW_VELOCITY = 11;
	public static final int FAST_VELOCITY = 15;
	public static final int SAFE_DISTANCE = 0;
	public static ArrayList<AggressiveCar> cars = new ArrayList<AggressiveCar>();
	static Random rand;
	public static void main(String[] args) {
		rand = new Random(2014);
		lanes = new ArrayList<ArrayList<AggressiveCar>>();
		for(int i = 0; i < LANE_LENGTH; i++)
			lanes.add(new ArrayList<AggressiveCar>());
		doFrame();
	}
	public static void insertCar(AggressiveCar car){
		lanes.get(0).add(car);
	}
	public static boolean safe(AggressiveCar cur, AggressiveCar front, double aggression){
		int distance = front.position - cur.position - 1;
		return distance >= (2 - aggression) * cur.myVelocity;
	}
	public static void doFrame(){
		Scanner s = new Scanner(System.in);
		int counter = 0;
		insertAggressiveCar(0, 6, 11, 0.7);
		insertAggressiveCar(0, 8, 15, 0.9);
		insertAggressiveCar(0, 9, 14, 0.3);
		insertAggressiveCar(0, 10, 13, 0.7);
		
		while(true){
			//if((cars.size() <= NUM_CARS) && counter % 1 == 0) insertCar();
			System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			moveCars();
			printCars();
			s.nextLine();
			counter++;
			System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		}
	}
	public static void insertCar(int lane, int pos, int speed){
		AggressiveCar newCar = new AggressiveCar(speed, 0.5);
		newCar.position = pos;
		newCar.lane = lane;
		cars.add(newCar);
		insertCar(newCar);

		System.out.println("Finished Inserting: " + newCar);
	}
	public static void insertAggressiveCar(int lane, int pos, int speed, double aggression){
		AggressiveCar newCar = new AggressiveCar(speed, aggression);
		newCar.position = pos;
		newCar.lane = lane;
		cars.add(newCar);
		insertCar(newCar);

		System.out.println("Finished Inserting: " + newCar);
	}
	public static void insertCar(){
		
		AggressiveCar newCar = new AggressiveCar(SLOW_VELOCITY + rand.nextInt(2) * (FAST_VELOCITY - SLOW_VELOCITY), 0.5);
		cars.add(newCar);
		insertCar(newCar);
		System.out.println(newCar);
	}
	static void moveCars(){
		Collections.sort(cars);
		for(int i = 0; i < lanes.size(); i++){
			ArrayList<AggressiveCar> eachLane = lanes.get(i);
			Collections.sort(eachLane);
		}
		for(AggressiveCar i: cars){
			printCars();
			moveCar(i, lanes.get(i.lane).indexOf(i));
		}
	}
	static void moveCar(AggressiveCar currentCar, int j){
		boolean aggressionLevelReached = rand.nextDouble() < currentCar.aggression;
		System.out.println("MOVECAR: " + currentCar + "\t\tDoesn't want to switch:" + !aggressionLevelReached);
		if(currentCar.lane == 0){  //if right lane
			if(j == 0){  // if first car in lane
				currentCar.regularMove();  // then just move forward
			}else{  //if not first car
				AggressiveCar carAhead = lanes.get(currentCar.lane).get(j - 1);  //get the car ahead
				int gap = carAhead.position - currentCar.position - 1;  //get distance between the two cars
				int futureDistance = gap - currentCar.myVelocity;  //get future distance after curCar moves
				if (safe(currentCar, carAhead, currentCar.aggression)){  //enough space for another move SAFE
					currentCar.regularMove();  	//move forward
				}else{  						//not enough space, try to pass
					AggressiveCar carAheadLeftLane = null;
					AggressiveCar carBehindLeftLane = null;
					for(AggressiveCar c: lanes.get(currentCar.lane + 1)){
						if(c.position >= currentCar.position){
							carAheadLeftLane = c;
						}
					}
					for(AggressiveCar c: lanes.get(currentCar.lane + 1)){
						if(c.position < currentCar.position){
							carBehindLeftLane = c;
							break;
						}
					}
					//get cars infront and behind
					if(!aggressionLevelReached || carBehindLeftLane != null &&   // cannot pass due to car behind in last lane
							carBehindLeftLane.position
							- currentCar.position + 1  //future distance 
							- currentCar.myVelocity > -carBehindLeftLane.myVelocity){  //SAFE
						currentCar.deaccelerateToSafeSpeed(currentCar, carAhead);  //stay in this lane at slower speed
					}else if((carAheadLeftLane == null || safe(currentCar, carAheadLeftLane, currentCar.aggression))){  // can switch to left lane at full speed
						currentCar.switchLeft();  //switch at normal speed
					}else if(carAheadLeftLane.position
							- currentCar.position -1
							- currentCar.myVelocity < futureDistance){
						currentCar.deaccelerateToSafeSpeedLeft(currentCar, carAheadLeftLane);
					}else{
						currentCar.deaccelerateToSafeSpeed(currentCar, carAhead);
					}
				}
			}
		}
		else if (currentCar.lane==1){  //left lane
			AggressiveCar carAheadRightLane = null;
			AggressiveCar carBehindRightLane = null;
			for(AggressiveCar c: lanes.get(currentCar.lane - 1)){
				if(c.position >= currentCar.position){
					carAheadRightLane = c;
				}
			}
			for(AggressiveCar c: lanes.get(currentCar.lane - 1)){
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
			if(carAheadRightLane == null || safe(currentCar, carAheadRightLane, currentCar.aggression)){  //check to see if can join right
				if(carBehindRightLane == null || safe(carBehindRightLane, currentCar, currentCar.aggression)){
					currentCar.switchRight();
				}
			}else{
				if(j > 0){
					AggressiveCar carAhead = lanes.get(currentCar.lane).get(j-1);
					int gap = carAhead.position - currentCar.position - 1;
					int futureDistance = gap + carAhead.myVelocity - currentCar.myVelocity;

					if(safe(currentCar, carAhead, currentCar.aggression)){
						currentCar.regularMove();
					}else{
						currentCar.deaccelerateToSafeSpeed(currentCar, carAhead);
					}
				}else{
					currentCar.regularMove();
				}
			}
		}
		updateFuturePosition(currentCar, j);
	}
	public static void updateFuturePosition(AggressiveCar c, int j){
		c.position = c.futurePosition;
		c.lane = c.futureLane;
		for(int i = 0; i < lanes.size(); i++)
			lanes.get(i).clear();

		for(AggressiveCar car : cars){
			lanes.get(car.lane).add(car);
		}

		for(int i = 0; i < lanes.size(); i++){
			ArrayList<AggressiveCar> eachLane = lanes.get(i);
			Collections.sort(eachLane);
		}
	}
	public static void updatePosition(){
		for(int i = 0; i < lanes.size(); i++){
			for(int j = 0; j < lanes.get(i).size(); j++){
				AggressiveCar currentCar = lanes.get(i).get(j);
				currentCar.position = currentCar.futurePosition;
				currentCar.lane = currentCar.futureLane;
			}
			lanes.get(i).clear();
		}

		for(AggressiveCar car : cars){
			int newLanes = car.lane;
			lanes.get(newLanes).add(car);
		}

		for(int i = 0; i < lanes.size(); i++){
			ArrayList<AggressiveCar> eachLane = lanes.get(i);
			Collections.sort(eachLane);
		}
	}

	public static void printCarsGraphical(){
		int maxPosition = 0; 
		for (AggressiveCar car : cars){
			maxPosition = Math.max(maxPosition , car.position);
		}

		AggressiveCar[][] gridForPrint = new AggressiveCar[lanes.size()][maxPosition+1]; 

		String lane = "";
		for(int i = 0; i < lanes.size(); i++){
			for(int j = 0; j < lanes.get(i).size(); j++){
				AggressiveCar currentCar = lanes.get(i).get(j);
				gridForPrint[i][currentCar.position] = currentCar;
			}
		}

		for(int i= 0; i < lanes.size(); i++){
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
//		for(int i = 0; i < cars.size(); i++){
//			System.out.print(cars.get(i));
//		}
//		System.out.println();
		for(ArrayList<AggressiveCar> i: lanes){
			for(AggressiveCar j: i){
				System.out.print(j);
			}
			System.out.println();
		}
	}


}
