import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class SimulateTimeSpace {
	// 001 Basic Model <WOW THIS LOOKS VERY GOOD> <WOW THIS LOOKS VERY GOOD> <WOW THIS LOOKS VERY GOOD> <WOW THIS LOOKS VERY GOOD> <WOW THIS LOOKS VERY GOOD>
	// 002 Add Safe Distance
	// 003 Add Loop (applies to previous versions) No change in Simulate
	// 004 Aggressiveness - Uses AggressiveCar instead of AggressiveCar, only affects
	// 005 Aggressiveness - Uses Aggressiveness to determine rate of switching TODO: make it possible to have a car crash
	// 006 Aggressiveness - Uses Aggressiveness to determine safety distance (need to have car crashes)
	// 007 Create a bimodal distribution, ratio of slow and fast, aggression generator
	// 008 Stable build.
	// 009 fixed bugs
	static ArrayList<ArrayList<AggressiveCar>> lanes;
	public static final int LANE_LENGTH = 2;
	public static final int SLOW_VELOCITY = 5;
	public static final int FAST_VELOCITY = 8;
	public static final double RATIO = 0.5;  //slow to fast	
	public static double density;
	
	static Scanner s = new Scanner(System.in);
	public static ArrayList<AggressiveCar> cars;
	static Random rand;
	static PrintWriter writer;
	public static ArrayList<int[]> shuffledPositions(){
		ArrayList<int[]> positions = new ArrayList<int[]>();
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < Car.MAX_LENGTH; j++){
				int[] newArray = new int[2];
				newArray[0] = i;
				newArray[1] = j;
				positions.add(newArray);
			}
		}
		Collections.shuffle(positions);
		return positions;
	}
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		rand = new Random(2014);
		writer = new PrintWriter("1-time-space-right.csv", "UTF-8");
		density = 0.1;
		cars = new ArrayList<AggressiveCar>();
		lanes = new ArrayList<ArrayList<AggressiveCar>>();
		for(int i = 0; i < LANE_LENGTH; i++)
			lanes.add(new ArrayList<AggressiveCar>());
		doFrame();
		writer.close();
	}
	public static double genAggression(){
		double aggression = rand.nextGaussian() / 3 + 0.5;
		if(aggression > 1) return 1;
		if(aggression < 0) return 0;
		return aggression;
	}
	public static void insertCar(AggressiveCar car){
		lanes.get(car.lane).add(car);
	}
	public static boolean safe(AggressiveCar cur, AggressiveCar front, double aggression){
		int distance = (front.position + Car.MAX_LENGTH - cur.position - 1) % Car.MAX_LENGTH;
		return distance >= (2.5 - aggression) * cur.getVelocity() + 2;
	}
	public static void doFrame(){
		int counter = 0;
		vsTotal = 0;
		vsCounter = 0;
		vNorm = 0;
		ArrayList<int[]> newPositions = shuffledPositions();
		while((cars.size() <= density * LANE_LENGTH * Car.MAX_LENGTH)){
			int[] pos = newPositions.remove(0);
			insertAggressiveCar(pos[0], pos[1]);
		}
		System.out.println(cars.size());
		while(counter < 600){
			counter++;
			//////System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			moveCars();
			////printCars();
			////s.nextLine();
			//////System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			if(counter > 100){
				for(AggressiveCar i: cars)
					if(i.lane == 0)
						writer.println(counter + "," + i.position);
				////printCars();
				////s.nextLine();
			}
			//////System.out.println(counter);
		}
		
		//////System.out.println("WRITTEN");
	}
	static double vsTotal;
	static double vsCounter;
	static double vNorm;
	public static double calculateV(){
		double total = 0;
		for(AggressiveCar i: cars){
			total += i.lastVelocity;
			////System.out.println(i.lastVelocity);
		}
		//printCars();
		//s.nextLine();
		total = total / cars.size();
		return total;
	}
	public static double calculateVS(){
		double total = 0;
		for(AggressiveCar i: cars){
			total += 1.0 / i.lastVelocity;
			////System.out.println(i.lastVelocity);
		}
		//printCars();
		//s.nextLine();
		total = (double) cars.size() / total;
		return total;
	}
	public static void insertCar(int lane, int pos, int speed){
		AggressiveCar newCar = new AggressiveCar(speed, 0.5);
		newCar.position = pos;
		newCar.lane = lane;
		cars.add(newCar);
		insertCar(newCar);

		//////System.out.println("Finished Inserting: " + newCar);
	}
	public static void insertAggressiveCar(int lane, int pos, int speed, double aggression){
		AggressiveCar newCar = new AggressiveCar(speed, aggression);
		newCar.position = pos;
		newCar.lane = lane;
		cars.add(newCar);
		insertCar(newCar);

		//////System.out.println("Finished Inserting: " + newCar);
	}
	public static void insertAggressiveCar(int lane, int pos){
		int speed;
		if(rand.nextDouble() < RATIO / (RATIO + 1)){
			speed = SLOW_VELOCITY;
		}else{
			speed = FAST_VELOCITY;
		}
		double aggression = genAggression();
		AggressiveCar newCar = new AggressiveCar(speed, aggression);
		newCar.position = pos;
		//System.out.println("lane: " + lane);
		newCar.lane = lane;
		cars.add(newCar);
		insertCar(newCar);

		//////System.out.println("Finished Inserting: " + newCar);
	}
	public static void insertAggressiveCar(){
		int speed;
		if(rand.nextDouble() < RATIO / (RATIO + 1)){
			speed = SLOW_VELOCITY;
		}else{
			speed = FAST_VELOCITY;
		}
		double aggression = genAggression();
		AggressiveCar newCar = new AggressiveCar(speed, aggression);
		newCar.position = 0;
		newCar.lane = 0;
		cars.add(newCar);
		insertCar(newCar);

		//////System.out.println("Finished Inserting: " + newCar);
	}
	public static void insertCar(){

		AggressiveCar newCar = new AggressiveCar(SLOW_VELOCITY + rand.nextInt(2) * (FAST_VELOCITY - SLOW_VELOCITY), 0.5);
		cars.add(newCar);
		insertCar(newCar);
		//////System.out.println(newCar);
	}
	static void moveCars(){
		Collections.sort(cars);
		for(int i = 0; i < lanes.size(); i++){
			ArrayList<AggressiveCar> eachLane = lanes.get(i);
			Collections.sort(eachLane);
		}
		for(AggressiveCar i: cars){
			////printCars();
			moveCar(i, lanes.get(i.lane).indexOf(i));
		}
	}
	static void moveCar(AggressiveCar currentCar, int j){
		boolean aggressionLevelReached = rand.nextDouble() < currentCar.aggression;
		//////System.out.println("MOVECAR: " + currentCar + "\t\tDoesn't want to switch:" + !aggressionLevelReached);
		if(currentCar.lane == 0){  //if right lane
			//////System.out.println("j: " + j);
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
							- currentCar.getVelocity() > -carBehindLeftLane.myVelocity){  //SAFE
						currentCar.deaccelerateToSafeSpeed(currentCar, carAhead);  //stay in this lane at slower speed
					}else if(aggressionLevelReached && (carAheadLeftLane == null || safe(currentCar, carAheadLeftLane, currentCar.aggression))){  // can switch to left lane at full speed
						currentCar.switchLeft();  //switch at normal speed
					}else if(aggressionLevelReached && carAheadLeftLane.position
							- currentCar.position - 1
							- currentCar.getVelocity() > futureDistance){
						currentCar.deaccelerateToSafeSpeedLeft(currentCar, carAheadLeftLane);
					}else{
						currentCar.deaccelerateToSafeSpeed(currentCar, carAhead);
					}
				}
			}
		}
		else if (currentCar.lane == 1){  //left lane
			//System.out.println("futureLane: " + currentCar.futureLane);
			AggressiveCar carAheadRightLane = null;
			AggressiveCar carBehindRightLane = null;
			for(AggressiveCar c: lanes.get(currentCar.lane - 1)){
				if(c.position >= currentCar.position){
					carAheadRightLane = c;
				}
			}
			for(AggressiveCar c: lanes.get(currentCar.lane - 1)){
				if(c.position < currentCar.position){
					carBehindRightLane = c;
				}
				break;
			}
			if((carBehindRightLane != null && safe(carBehindRightLane, currentCar, currentCar.aggression))){
				if(j > 0){
					AggressiveCar carAhead = lanes.get(currentCar.lane).get(j-1);
					int gap = carAhead.position - currentCar.position - 1;
					int futureDistance = gap + carAhead.getVelocity() - currentCar.getVelocity();

					if(safe(currentCar, carAhead, currentCar.aggression)){
						currentCar.regularMove();
					}else{
						currentCar.deaccelerateToSafeSpeed(currentCar, carAhead);
					}
				}else{
					currentCar.regularMove();
				}
			}else{
				if(carAheadRightLane == null || safe(currentCar, carAheadRightLane, currentCar.aggression)){  //check to see if can join right
					currentCar.switchRight();
					//System.out.println("SWITCHED RIGHT!");
				}else{
					currentCar.deaccelerateToSafeSpeedRight(currentCar, carAheadRightLane);
				}
			}
		}
		updateFuturePosition(currentCar, j);
	}
	public static void updateFuturePosition(AggressiveCar c, int j){
		c.position = c.futurePosition;
		//System.out.println("future pos: " + c.futureLane);
		c.lane = c.futureLane;
		for(int i = 0; i < lanes.size(); i++)
			lanes.get(i).clear();

		for(AggressiveCar car : cars){
			//////System.out.println("ADDED INTO LANE " + car.lane);
			//System.out.println(car.lane);
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
			////System.out.print(thisLane+"\n");
		}
		////System.out.println(lane);
	}
	public static void printCars(){
		//		for(int i = 0; i < cars.size(); i++){
		//			//////System.out.print(cars.get(i));
		//		}
		//		//////System.out.println();
		for(ArrayList<AggressiveCar> i: lanes){
			for(AggressiveCar j: i){
				////System.out.print(j);
			}
			//System.out.println();
		}
	}


}
